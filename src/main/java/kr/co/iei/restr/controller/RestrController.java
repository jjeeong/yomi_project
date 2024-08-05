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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.iei.member.model.dto.Member;
import kr.co.iei.restr.model.dto.BlogSearchResult;
import kr.co.iei.restr.model.dto.Restaurant;
import kr.co.iei.restr.model.service.RestrService;

@Controller
@RequestMapping(value = "/restaurant")
public class RestrController {
	@Autowired
	private RestrService restrService;

	@Value("${file.root}")
	private String root; // application.properties에 설정되어있는 file.root값을 가지고와서 문자열로 지정

	//맛집 상세 페이지
	@GetMapping(value = "/restrView")
    public String restrView(Model model, int restrNo) {
        Restaurant r = restrService.selectOneRestr(restrNo); // 테스트용으로 1번 쿼리를 검색하도록 설정해둠.

        if (r == null) {
            return "redirect:/";
        } else {
            String searchResult = restrService.searchBlog(r.getRestrName());
            List<BlogSearchResult> searchResults = parseSearchResults(searchResult);
            
            List list = restrService.selectRestrMenu(restrNo);
            r.setRestrMenu(list);

            model.addAttribute("r", r);
            model.addAttribute("searchResults", searchResults);
            return "restaurant/restrView";
        }
    }

	//블로그 검색 결과
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
    
    //맛집 리스트
    @GetMapping(value = "/restrList")
    public String restrList(Model model) {
        int restrTotalCount = restrService.selectRestrTotalCount();
        model.addAttribute("restrTotalCount", restrTotalCount);
        return "restaurant/restrList";
    }
    
    @ResponseBody
	@GetMapping(value = "/more")
	public List photoMore(int start, int amount) {
		List list = restrService.selectRestrList(start, amount);
		return list;
	}

	@ResponseBody
	@PostMapping(value = "/likePush")
	public int likePush(int restrNo, int isLike, @SessionAttribute(required = false) Member member) {
		// @SessionAttribute에서 로그인정보를 가지고올 때 required옵션을 명시하지않으면 기본적으로 true
		// -> 로그인이 되어있지 않으면 에러가 발생
		// -> 로그인이 되어있지 않은 상태에서 에러를 발생시키지 않으려면 (required = false)옵션을 추가
		// -> 로그인이 되어있으면 로그인 한 회원정보/로그인이 되어있지 않으면 null
		if (member == null) {
			return -10;
		} else {
			int memberNo = member.getMemberNo();
			int result = restrService.likePush(restrNo, isLike, memberNo);
			return result;
		}
	}
	
	@GetMapping(value = "/writeFrm")
	public String writeFrm() {
		return "restaurant/restrWriteFrm";
	}// restFrm()

	@GetMapping(value = "/updateFrm")
	public String updateFrm() {
		return "restaurant/restrUpdateFrm";
	}// restFrm()
	
	
	@PostMapping(value = "/writeReview")
	public String writeReview() {
//		int result = restrService.writeReview();
		return "/restaurant/restrView?restrNo=21";
	}
	
	@ResponseBody
	@PostMapping(value = "/writeReviewFrm")
	public int writeReviewFrm(@RequestParam int restrNo, @SessionAttribute(required = false) Member member) {
	    if (member == null) {
	        return -10;
	    } else {
	        return 1;
	    }
	}
}

