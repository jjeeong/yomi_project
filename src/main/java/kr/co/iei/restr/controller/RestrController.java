package kr.co.iei.restr.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.iei.member.model.dto.Member;
import kr.co.iei.restr.model.dto.BlogSearchResult;
import kr.co.iei.restr.model.dto.Restaurant;

import kr.co.iei.restr.model.dto.RestrMenu;
import kr.co.iei.restr.model.dto.RestrTag;
import kr.co.iei.restr.model.dto.Review;
import kr.co.iei.restr.model.service.RestrService;
import kr.co.iei.util.FileUtils;

@Controller
@RequestMapping(value = "/restaurant")
public class RestrController {
	@Autowired
	private RestrService restrService;

	@Autowired
	private FileUtils fileUtils;
	
	@Value("${file.root}")
	private String root; // application.properties에 설정되어있는 file.root값을 가지고와서 문자열로 지정

	// 맛집 상세 페이지
	@GetMapping(value = "/restrView")
	public String restrView(Model model, int restrNo) {
		Restaurant r = restrService.selectOneRestr(restrNo); // 테스트용으로 1번 쿼리를 검색하도록 설정해둠.

		if (r == null) {
			return "redirect:/";
		} else {
			//블로그 API
			String searchResult = restrService.searchBlog(r.getRestrName());
			List<BlogSearchResult> searchResults = parseSearchResults(searchResult);

			//메뉴 리스트
			List menuList = restrService.selectRestrMenu(restrNo);
			r.setRestrMenu(menuList);
			
			List tagList = restrService.selectRestrTag(restrNo);
			r.setRestrTag(tagList);

			//맛집 정보
			model.addAttribute("r", r);
			model.addAttribute("searchResults", searchResults);
			
			//리뷰 카운트
			int reviewCount = restrService.reviewCount(restrNo);
			model.addAttribute("reviewCount", reviewCount);
			return "restaurant/restrView";
		}
	}

	// 블로그 검색 결과
	private List<BlogSearchResult> parseSearchResults(String searchResult) {
		List<BlogSearchResult> results = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(searchResult);
			JsonNode items = root.path("items");
			if (items.isArray()) {
				for (JsonNode item : items) {
					BlogSearchResult result = new BlogSearchResult();
					result.setTitle(item.path("title").asText());
					result.setLink(item.path("link").asText());
					result.setDescription(item.path("description").asText());
					result.setBloggerName(item.path("bloggername").asText());
					result.setPostDate(item.path("postdate").asText());
					results.add(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	// 맛집 리스트
	@GetMapping(value = "/restrList")
	public String restrList(Model model) {
		int restrTotalCount = restrService.selectRestrTotalCount();
		model.addAttribute("restrTotalCount", restrTotalCount);
		return "restaurant/restrList";
	}

	// 맛집 리스트 더보기
	@ResponseBody
	@GetMapping(value = "/more")
	public List photoMore(int start, int amount) {
		List list = restrService.selectRestrList(start, amount);
		return list;
	}

	// 맛집 좋아요
	@ResponseBody
	@PostMapping(value = "/likePush")
	public int likePush(int restrNo, int isLike, @SessionAttribute(required = false) Member member) {
		if (member == null) {
			return -10;
		} else {
			int memberNo = member.getMemberNo();
			int result = restrService.likePush(restrNo, isLike, memberNo);
			return result;
		}
	}
	
	// 리뷰 작성
	@PostMapping(value = "/writeReview")
	public String writeReview(@SessionAttribute(required = false) Member member, Review review, Restaurant restaurant, Double reviewStar) {

		int memberNo = member.getMemberNo();
		
		System.out.println(memberNo);
		
		review.setMemberNo(memberNo);
		review.setRestrNo(restaurant.getRestrNo());
		review.setReviewStar(reviewStar);
		
		int result = restrService.writeReview(review);
		return "redirect:/restaurant/restrView?restrNo=" + restaurant.getRestrNo();
	}

	// 리뷰 작성 페이지 로그인 확인
	@ResponseBody
	@PostMapping(value = "/writeReviewFrm")
	public int writeReviewFrm(@RequestParam int restrNo, @SessionAttribute(required = false) Member member) {
		if (member == null) {
			return -10;
		} else {
			return 1;
		}
	}
	
	// 맛집 리뷰 목록 불러오기
	@ResponseBody
	@GetMapping(value = "/reviewMore")
	public List reviewMore(int start, int amount, int restrNo) {
		List reviewList = restrService.selectReviewList(start, amount, restrNo);
		
		System.out.println(reviewList);
		return reviewList;
	}
	

	@GetMapping(value = "/writeFrm")
	public String writeFrm() {
		return "restaurant/restrWriteFrm";
	}// restFrm()

	@PostMapping(value = "/restrWrite")//실험 결과 : 여러개 쓸거면 그냥 일일이 받아서 합치자.. 뭔가 더 복잡해진다..
	public String write(Restaurant r, String[] menuName, int[] menuPrice, String[] tagName, MultipartFile imageFile1,
			MultipartFile imageFile2, Model model) {
		List<RestrMenu> menuList = new ArrayList<RestrMenu>();
		for (int i = 0; i < menuName.length; i++) {
			RestrMenu rm = new RestrMenu();
			rm.setRestrMenuName(menuName[i]);
			rm.setRestrMenuPrice(menuPrice[i]);
			menuList.add(rm);
		}
		String savepath = root+"/yomi/";
		String filepath1 = fileUtils.upload(savepath, imageFile1);
		String filepath2 = fileUtils.upload(savepath, imageFile2);
		r.setRestrImg1(filepath1);
		r.setRestrImg2(filepath2);
		int result = restrService.insertRestr(r, menuList, tagName);
		if (result > 0) {
			int restrNo = restrService.getRecentRestrNo();
			model.addAttribute("title", "등록 성공");
			model.addAttribute("msg", "맛집 리스트가 추가되었습니다!");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/restaurant/restrView?restrNo="+restrNo);
		} else {
			model.addAttribute("title", "등록 실패");
			model.addAttribute("msg", "맛집 리스트가 추가되었습니다!");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/restaurant/restrList");
		}
		return "common/msg2";
	}

	@GetMapping(value = "/updateFrm")
	public String updateFrm(int restrNo, Model model) {
		Restaurant r = restrService.selectOneRestrWith(restrNo);
		if(r==null) {
			model.addAttribute("title", "수정 불가");
			model.addAttribute("msg", "존재하지 않는 게시물 입니다.");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/restaurant/restrList");
			return "common/msg2";
		}else {
			model.addAttribute("r", r);
			return "restaurant/restrUpdateFrm";			
		}
	}// restFrm()
	
	@PostMapping(value="/restrUpdate")
	public String restrUpdate(Restaurant r, String[] menuName, int[] menuPrice, String[] tagName, MultipartFile imageFile1,
			MultipartFile imageFile2, int[] delTagNo, String filepath1, String filepath2, int[] delMenuNo, Model model) {
		String savepath = root+"/yomi/";
		//delTag, delMenu는 삭제하고 menu, Tag는 다시 삽입할거임(졸려서 기억 안날까봐..)
		int updateImgCount = 0;
		List<String> delImgFile = new ArrayList<String>();
		if(!imageFile1.isEmpty()) {
			String filepath3 = fileUtils.upload(savepath, imageFile1);
			r.setRestrImg1(filepath3);
			updateImgCount++; //1이 더해지면 restrImg1을 바꿔야하는 것
			delImgFile.add(filepath1);
		}//if
		if(!imageFile2.isEmpty()) {
			String filepath4 = fileUtils.upload(savepath, imageFile2);
			r.setRestrImg2(filepath4);
			updateImgCount+=2;//2가 더해지면 restrImg2를 바꿔야하는 것
			delImgFile.add(filepath2);
		}//if
		List<RestrMenu> menuList = new ArrayList<RestrMenu>();
		if(menuName!=null) {
			for (int i = 0; i < menuName.length; i++) {
				RestrMenu rm = new RestrMenu();
				rm.setRestrMenuName(menuName[i]);
				rm.setRestrMenuPrice(menuPrice[i]);
				menuList.add(rm);
			}			
		}
		int result = restrService.updateRestr(r, menuList, tagName, delMenuNo, delTagNo, updateImgCount);
		//service, dao까지 만들어서 실행해보고 만약 메뉴([]), 태그([]), 삭제할 파일 이름([])배열이 새로 추가된게 없을 시 input이 없어 오류가 날경우
		//=> 질문 결과, 배열의 경우 안들어오면 null로 들어오므로 상관 ㄴㄴ 하다고 함 
		//(단, int 변수 하나만 받아온다고 했을때, 안들어오면 null이 들어오고, null을 int로 형변환 할 수 없으므로 에러가 남)
		//만약 잘 되었다면 delImgFile 리스트 안의 파일들 삭제하기, 잘되지 않았으면 방금 업로드한 filepath3, 4 삭제하기
		if(result>0) {
			for(String delfilepath : delImgFile) {
				File delFile = new File(savepath+delfilepath);
				delFile.delete();
			}
			model.addAttribute("title", "수정 성공");
			model.addAttribute("msg", "수정에 성공하셨습니다.");
			model.addAttribute("icon", "success");
		}else {
			if(r.getRestrImg1() !=null) {
				File delFile = new File(savepath+r.getRestrImg1());
				delFile.delete();
			}
			if(r.getRestrImg2() !=null) {
				File delFile = new File(savepath+r.getRestrImg2());
				delFile.delete();
			}
			model.addAttribute("title", "수정 실패");
			model.addAttribute("msg", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
			model.addAttribute("icon", "error");
		}
		model.addAttribute("loc", "/restaurant/restrView?restrNo="+r.getRestrNo());
		//정원이와 협의 후에 조회수 안올리는거 생각하기
		return "common/msg2";
	}

}
