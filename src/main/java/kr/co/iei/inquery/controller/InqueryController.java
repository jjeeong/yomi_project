package kr.co.iei.inquery.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import kr.co.iei.inquery.model.dto.Inquery;
import kr.co.iei.inquery.model.dto.InqueryFile;
import kr.co.iei.inquery.model.dto.InqueryListData;
import kr.co.iei.inquery.model.service.InqueryService;
import kr.co.iei.member.model.dto.Member;
import kr.co.iei.util.FileUtils;


@Controller
@RequestMapping(value = "/inquery")
public class InqueryController {
	@Autowired
	private InqueryService inqueryService;
	
	@Value("$file.root}")
	private String root;
	
	@Autowired
	private FileUtils fileUtils; 
	
	@GetMapping(value = "/list")
	public String list(Model model, int reqPage) {
		InqueryListData ild = inqueryService.selectInqueryList(reqPage);
		model.addAttribute("list",ild.getList());
		model.addAttribute("pageNavi", ild.getPageNavi());
		return "inquery/list";
	}
	
	@GetMapping(value = "/editorFrm")
	public String editorFrm(@SessionAttribute(required = false) Member member) {
		if(member == null) {
			return "redirect:/member/loginFrm";
		}
		return "inquery/editorFrm";
	}
	
	@PostMapping(value = "write")
	public String write(Inquery inq, MultipartFile[] upfile, Model model) {
		
		System.out.println(inq);
		System.out.println("업로드된 파일 수 : " + upfile.length); 
		
		List<InqueryFile> fileList = new ArrayList<InqueryFile>();
		
		if(!upfile[0].isEmpty()) {
			//C:/Temp/upload/inquery/
			String savepath = root + "/inquery/";
			for(MultipartFile file : upfile) {
				String filename = file.getOriginalFilename();
				String filepath = fileUtils.upload(savepath, file);
				InqueryFile inqueryFile = new InqueryFile();
				inqueryFile.setFilename(filename);
				inqueryFile.setFilepath(filepath);
				fileList.add(inqueryFile);
			}
		}
		// inq : inqueryTile.inqueryWriter, inqueryContent
		// fileList : (InqueryFile) x 첨부파일갯수
		int result = inqueryService.insertInquery(inq,fileList);
		if(result>0) {
			model.addAttribute("title","작성성공!");
			model.addAttribute("msg","공지사항 작성에 성공했습니다.");
			model.addAttribute("icon","success");
			model.addAttribute("loc","/notice/list?reqPage=1");
			return "common/msg";
		}
		return "redirect:/inquery/editorFrm";
	}
	
	@GetMapping(value = "/view")
	public String view(int inqueryNo, String check, Model model, @SessionAttribute(required = false) Member member) {
		int memberNo = 0;
		if(member != null) {
			memberNo = member.getMemberNo();
		}
		//로그인이 되어있지않으면 memberNo = 0 / 로그인이 되어있으면 memberNo = 로그인한 회원번호
		Inquery inq = inqueryService.selectOneInquery(inqueryNo,check);
		if(inq== null) {
			model.addAttribute("title","조회실패");
			model.addAttribute("msg","해당 게시글이 존재하지 않습니다.");
			model.addAttribute("icon","info");
			model.addAttribute("loc","/notice/list?regPage=1");
			return "common/msg";
		}else {
			System.out.println(inq);
			model.addAttribute("inq",inq);		
			return "inquery/view";
		}		
	}
	
	
	
}







































