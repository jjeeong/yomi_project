package kr.co.iei;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.iei.board.model.service.BoardService;
import kr.co.iei.etc.MainList;
import kr.co.iei.restr.model.service.RestrService;

@Controller
public class HomeController {
	@Autowired
	private RestrService restrService;
	@Autowired
	private BoardService boardService;
	
		@GetMapping(value="/")
		public String main(Model model) {
			MainList mainList = new MainList();
			List restrList = restrService.selectBest();
			//List boardList = boardService.selectBest(); =>좋아요 수 구현되면 할것
			mainList.setBestRestrList(restrList);
			//mainList.setBestBoardList(boardList);
			model.addAttribute("main", "mainList");
			return "index";
		}
//		
//		@GetMapping(value="/ref")
//		public String ref() {
//			return "ref";
//		}
}
