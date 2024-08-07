package kr.co.iei.restr.controller;

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

import kr.co.iei.restr.model.dto.Review;
import kr.co.iei.restr.model.dto.ReviewImg;
import kr.co.iei.restr.model.dto.ReviewTag;
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
		Restaurant r = restrService.selectOneRestr(restrNo);

		if (r == null) {
			return "redirect:/";
		} else {
			//블로그 API
			String searchResult = restrService.searchBlog(r.getRestrName());
			List<BlogSearchResult> searchResults = parseSearchResults(searchResult);

			//메뉴 리스트
			List menuList = restrService.selectRestrMenu(restrNo);
			r.setRestrMenu(menuList);
			
			//태그 리스트
			List tagList = restrService.selectRestrTag(restrNo);
			r.setRestrTag(tagList);

			//맛집 정보
			model.addAttribute("r", r);
			model.addAttribute("searchResults", searchResults);
			
			//리뷰 카운트
			int reviewCount = restrService.reviewCount(restrNo);
			model.addAttribute("reviewCount", reviewCount);
			
			//맛집 별점
			Double starAvg = restrService.RestrStarAvg(restrNo);
			model.addAttribute("starAvg", starAvg);
			
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
	public String writeReview(@SessionAttribute(required = false) Member member, Review review, Restaurant restaurant, Double reviewStar,  
			@RequestParam(value = "keywords", required = false) String[] keywords, MultipartFile[] upfile) {
		// 작성자 번호
		int memberNo = member.getMemberNo();	
		review.setMemberNo(memberNo);
		review.setRestrNo(restaurant.getRestrNo());
		review.setReviewStar(reviewStar);
		
		//이미지 삽입
		int restrNo = restaurant.getRestrNo();		
		List<ReviewImg> reviewImgList = new ArrayList<ReviewImg>();
		
		System.out.println("upfile : "+upfile);
		
		//리뷰 번호 불러오기
		int reviewNo = restrService.selectOneReview(restrNo) + 1;
		
		review.setReviewNo(reviewNo);
		
		if(!upfile[0].isEmpty()) {
			String savepath = root+"/review/";
			for (MultipartFile file : upfile) {
				String reviewFilename = file.getOriginalFilename();
				String reviewFilePath = fileUtils.upload(savepath, file);
				ReviewImg reviewImg = new ReviewImg();
				reviewImg.setReviewFilename(reviewFilename);
				reviewImg.setReviewFilePath(reviewFilePath);
				reviewImgList.add(reviewImg);
				System.out.println("reviewImg : "+reviewImgList);
			}
		}
		
		//작성 결과 (img list도 함께 삽입)
		int result = restrService.writeReview(review, reviewImgList);
		
		//리뷰 키워드 삽입
		if (result > 0 && keywords != null) {			
			int tagResult = restrService.insertKeyword(reviewNo, keywords);
		}
	
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
		//리뷰 리스트 불러오기
		List<Review> reviewList = restrService.selectReviewList(start, amount, restrNo);
		
		for(Review review : reviewList) {
			int reviewNo = review.getReviewNo();
			List reviewTagList = restrService.selectReviewTagList(reviewNo);
			
			List reviewImgList = restrService.selectReviewImgList(reviewNo);
			
			review.setReviewTag(reviewTagList);
			review.setReviewImg(reviewImgList);
		}
		
		return reviewList;
	}
	

	@GetMapping(value = "/writeFrm")
	public String writeFrm() {
		return "restaurant/restrWriteFrm";
	}// restFrm()

	@PostMapping(value = "/restrWrite")
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
			return "/common/msg2";
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
		for (int i = 0; i < menuName.length; i++) {
			RestrMenu rm = new RestrMenu();
			rm.setRestrMenuName(menuName[i]);
			rm.setRestrMenuPrice(menuPrice[i]);
			menuList.add(rm);
		}
		int result = restrService.updateRestr(r, menuList, tagName, delMenuNo, delTagNo, updateImgCount);
		//service, dao까지 만들어서 실행해보고 만약 메뉴([]), 태그([]), 삭제할 파일 이름([])배열이 새로 추가된게 없을 시 input이 없어 오류가 날경우, 업데이트 html에 미리 하나씩 [0]넣어두고 다시 작업해보기
		//만약 잘 되었다면 delImgFile 리스트 안의 파일들 삭제하기, 잘되지 않았으면 방금 업로드한 filepath3, 4 삭제하기
		return "restaurant/restrView?restrNo="+r.getRestrNo();
	}

}
