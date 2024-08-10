package kr.co.iei;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.iei.board.model.service.BoardService;
import kr.co.iei.restr.model.service.RestrService;

@Controller
public class HomeController {
	@Autowired
	private RestrService restrService;
	
		@GetMapping(value="/")
		public String main(Model model) {
			List restrList = restrService.selectBest();
			List reviewList = restrService.selectBestReview();
			model.addAttribute("restrList", restrList);
			model.addAttribute("reviewList", reviewList);
			return "index";
		}
//		
//		@GetMapping(value="/ref")
//		public String ref() {
//			return "ref";
//		}
}
