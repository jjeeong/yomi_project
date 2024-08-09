package kr.co.iei.board.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import kr.co.iei.board.model.dto.Board;
import kr.co.iei.board.model.dto.BoardFile;
import kr.co.iei.board.model.dto.BoardListData;
import kr.co.iei.board.model.service.BoardService;
import kr.co.iei.member.model.dto.Member;
import kr.co.iei.util.FileUtils;

@Controller
@RequestMapping(value="/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Value("${file.root}")
	private String root;
	
	@GetMapping(value="/list")
	public String list(Model model, int reqPage) {
		BoardListData bld  = boardService.selectBoardList(reqPage);
		model.addAttribute("list",bld.getList());
		model.addAttribute("pageNavi", bld.getPageNavi());
		return "board/list";
	}
	
	@GetMapping(value="/writeFrm")
	public String writeFrm() {
		return "board/writeFrm";
	}
	
	@ResponseBody
	@PostMapping(value="/editorImage", produces ="plain/text;charset=utf-8")
	public String editorImage(MultipartFile upfile) {
		String savepath = root+"/board/editor/";
		String filepath = fileUtils.upload(savepath, upfile);
		return "/board/editor/"+filepath;
	}
	
	@PostMapping(value="/write")
	public String write(Board b, MultipartFile upfile,Model model) {
		System.out.println(b);
		System.out.println("업로드 된 파일의 수 : " + upfile);
		
		List<BoardFile> fileList = new ArrayList<BoardFile>();
		
					
			String savepath = root+"/board/";
			
				//사용자가 업로드한 파일 이름 출력 
				String filepath = fileUtils.upload(savepath, upfile);
				b.setThumbNailImg(filepath);
		int result = boardService.insertBoard(b);
		if(result > 0) {
			model.addAttribute("title", "작성성공!");
			model.addAttribute("msg", "게시글 작성에 성공했습니다.");
			model.addAttribute("icon","success");
			model.addAttribute("loc","/board/list?reqPage=1");
		return "common/msg";
		}
		return "redirect:/board/writreFrm";
	}
	
	@GetMapping(value="/view")
	public String view(int boardNo, String check, Model model,@SessionAttribute(required = false) Member member) {
		int memberNo = 0;
		if(member != null) {
			memberNo = member.getMemberNo();
		}
		System.out.println("check : "+check);
		Board b = boardService.selectOneBoard(boardNo,check,memberNo);
		if(b == null) {
			model.addAttribute("title","조회실패");
			model.addAttribute("msg","해당 게시글이 존재하지 않습니다.");
			model.addAttribute("icon","info");
			model.addAttribute("loc","/board/list?reqPage=1");
			return "common/msg";
		}else {
			model.addAttribute("b",b);
			return "board/view";
		}				
	}
}		