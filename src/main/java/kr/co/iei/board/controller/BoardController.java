package kr.co.iei.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.iei.board.model.service.BoardService;

@Controller
@RequestMapping(value="/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@Value("${file.root}")
	private String root;
	
	@GetMapping(value="/list")
	public String list(Model model) {
		List list = boardService.selectBoardList();
		model.addAttribute("list",list);
		
		return "/board/list";
	}
	
	@GetMapping(value="/writeFrm")
	public String writeFrm() {
		return "board/writeFrm";
	}

}
