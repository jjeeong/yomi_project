package kr.co.iei;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.iei.board.model.service.BoardService;
import kr.co.iei.restr.model.service.RestrService;
import kr.co.iei.util.CookieUtils;

@Controller
public class HomeController {
	@Autowired
	private RestrService restrService;
	@Autowired
	private CookieUtils cookieUtils;
	
		@GetMapping(value="/")
		public String main(Model model, HttpServletRequest request) {
			List restrList = restrService.selectBest();
			List reviewList = restrService.selectBestReview();
			model.addAttribute("restrList", restrList);
			model.addAttribute("reviewList", reviewList);
			List<Integer>restrNoList = new ArrayList<Integer>();
			String cookieValue=cookieUtils.getCookieValue(request, "restrNo");
			System.out.println(cookieValue.toString());
			if(cookieValue !=null) {
				String[] cookieString = cookieValue.split("/");
				for(int i=0; i<cookieString.length; i++) {
					restrNoList.add(Integer.parseInt(cookieString[i]));
					System.out.println(cookieString[i]);
				}
			}
			List recentList = restrService.selectRecent(restrNoList);
			System.out.println(recentList.toString());
			model.addAttribute("recentList", recentList);
			return "index";
		}
//		
//		@GetMapping(value="/ref")
//		public String ref() {
//			return "ref";
//		}
}
