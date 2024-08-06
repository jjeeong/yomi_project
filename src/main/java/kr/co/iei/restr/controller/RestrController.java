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
		String memberName = member.getMemberName();
		
		System.out.println(memberNo);
		System.out.println(memberName);
		
		review.setMemberNo(memberNo);
		review.setRestrNo(restaurant.getRestrNo());
		review.setReviewStar(reviewStar);
		review.setMemberName(memberName);
		
		System.out.println("review는 어케 됐을까요? : " + review);
		
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
		String savepath = root+"/restr/";
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
	public String updateFrm() {
		return "restaurant/restrUpdateFrm";
	}// restFrm()

}
