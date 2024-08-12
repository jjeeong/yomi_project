package kr.co.iei.inquery.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.iei.inquery.model.dto.Inquery;
import kr.co.iei.inquery.model.dto.InqueryComment;
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
	
	@Value("${file.root}")
	private String root;
	
	@Autowired
	private FileUtils fileUtils; 

		
	@GetMapping(value = "/list")
	public String list(Model model, int reqPage, @SessionAttribute(required = false) Member member) {
	
		
		if(member == null) {
			InqueryListData ild = inqueryService.selectInqueryList(reqPage);
			model.addAttribute("list",ild.getList());
			model.addAttribute("pageNavi", ild.getPageNavi());
		}else{
			InqueryListData ild = inqueryService.selectInqueryList(reqPage, member);
			model.addAttribute("list",ild.getList());
			model.addAttribute("pageNavi", ild.getPageNavi());
		}
		
		
		
		
		return "inquery/list";
	}
	
	@GetMapping(value = "/editorFrm")
	public String editorFrm(@SessionAttribute(required = false) Member member) {		
		return "inquery/editorFrm";
	}
	

	//2. updateFrm, editorFrm 로부터 호출 
	@ResponseBody
	@PostMapping(value = "/editorImage", produces = "text/plain;charset=utf-8")
	public String editorImage(MultipartFile upfile) {
		String savepath = root + "/inquerySummernote/";
		String filepath = fileUtils.upload(savepath, upfile);
		return "/inquerySummernote/" + filepath;
	}
	
	
	
	@PostMapping(value = "/write")
	public String write(Inquery inq, MultipartFile[] upfile, Model model, Integer open) {
		
		//open = 0 : 공개 X / open = 1 : 공개 O
		if(open == null) {
			open = 0;
		}else{
			open = 1;
		}
		
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
		int result = inqueryService.insertInquery(inq,fileList,open);	
		if(result>0) {
			model.addAttribute("title","작성성공!");
			model.addAttribute("msg","공지사항 작성에 성공했습니다.");
			model.addAttribute("icon","success");
			model.addAttribute("loc","/inquery/list?reqPage=1");
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
		Inquery inq = inqueryService.selectOneInquery(inqueryNo,check,memberNo);
		//System.out.println("inq:"+inq);
		if(inq== null) {
			model.addAttribute("title","조회실패");
			model.addAttribute("msg","해당 게시글이 존재하지 않습니다.");
			model.addAttribute("icon","info");
			model.addAttribute("loc","/inquery/list?regPage=1");
			return "common/msg";
		}else {
			model.addAttribute("inq",inq);
			return "inquery/view";
		}		
	}
	
	
	@GetMapping(value = "/filedown")
	public void filedown(InqueryFile inqueryFile, HttpServletResponse response) {
		String savepath = root + "/inquery/";
		fileUtils.downloadFile(savepath, inqueryFile.getFilename(), inqueryFile.getFilepath(),response);
	}
	
	@GetMapping(value = "/delete")
	public String delete(int inqueryNo, Model model) {
		// 삭제를 하게되면 해당 게시글의 첨부파일도 삭제 -> 삭제결과로 파일 목록을 가져옴
		List<InqueryFile> list = inqueryService.deleteInquery(inqueryNo);
		if(list == null) {
			model.addAttribute("title", "삭제 실패");
			model.addAttribute("msg", "해당 게시글이 존재하지 않습니다..");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/inquery/list?reqPage=1");
		}else {
			String savepath = root + "/inquery/";
			for (InqueryFile file : list) {
				File delFile = new File(savepath + file.getFilepath());
				delFile.delete();
			}
			model.addAttribute("title", "삭제 성공");
			model.addAttribute("msg", "게시글이 삭제되었습니다..");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/inquery/list?reqPage=1");
		}
		return "common/msg";		
	}
	
	@GetMapping(value = "/updateFrm")
	public String updateFrm(int inqueryNo, Model model) {
		Inquery inq = inqueryService.getOneInquery(inqueryNo);
		model.addAttribute("inq",inq);
		return "inquery/updateFrm";
	}
	
	@PostMapping(value = "/update")
	public String update(Inquery inq, MultipartFile[] upfile, int[] delFileNo, Model model, String open) {
		
		//open = 0 : 공개 X / open = 1 : 공개 O
		if(open == null) {
			open = "0";
		}else {
			open = "1";
		}
		
		// 새로 추가된 파일 업로드 작업
		//System.out.println("no"+inq.getInqueryNo());
		//System.out.println("upfile:"+upfile);
		List<InqueryFile> fileList = new ArrayList<InqueryFile>();
		String savepath = root + "/inquery/";
		if(!upfile[0].isEmpty()) {
			for(MultipartFile file : upfile) {
				String fileName = file.getOriginalFilename();
				String filePath = fileUtils.upload(savepath,file);
				InqueryFile inqueryFile = new InqueryFile();
				inqueryFile.setFilename(fileName);
				inqueryFile.setFilepath(filePath);
				inqueryFile.setInqueryNo(inq.getInqueryNo());
				fileList.add(inqueryFile);
			}
		}
		//System.out.println("fileList:"+fileList);
		// 수정요청하면서 데이터 4개전달 (n : inquery테이블 수정, fileList : inquery_file추가, delFileNo : inquery_file 삭제용, open : 공개유무)
		List<InqueryFile> delFileList = inqueryService.updateInquery(inq, fileList, delFileNo, open);
		if(delFileList == null) {
			model.addAttribute("title", "수정 실패");
			model.addAttribute("msg", "처리중 문제가 발생했습니다.. 잠시후 다시 시도해주세요..");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/inquery/list?reqPage=1");
			return "common/msg";
		}else {
			for (InqueryFile inqueryFile : delFileList) {
				File delFile = new File(savepath + inqueryFile.getFilepath());
				delFile.delete();
			}
			return "redirect:/inquery/view?inqueryNo=" + inq.getInqueryNo();
		}
	}
	
	
	@PostMapping(value = "/insertComment")
	public String insertComment(InqueryComment ic, Model model) {
		int result = inqueryService.insertComment(ic);
		//System.out.println(ic);
		//System.out.println(result);
		if (result > 0) {
			model.addAttribute("title", "댓글 작성 성공");
			model.addAttribute("msg", "댓글이 작성 되었습니다");
			model.addAttribute("icon", "success");
		} else {
			model.addAttribute("title", "댓글 작성 실패");
			model.addAttribute("msg", "댓글 작성 중 문제가 발생했습니다.");
			model.addAttribute("icon", "warning");
		}
		model.addAttribute("loc", "/inquery/view?check=1&inqueryNo=" + ic.getInqueryRef());
		return "common/msg";
	}
	
	@PostMapping(value = "/updateComment")
	public String updateComment(InqueryComment ic, Model model) {
		int result = inqueryService.updateComment(ic);
		if (result > 0) {
			model.addAttribute("title", "성공");
			model.addAttribute("msg", "댓글이 수정되었습니다!");
			model.addAttribute("icon", "success");
		} else {
			model.addAttribute("title", "실패");
			model.addAttribute("title", "잠시 후 다시 시도해주세요..");
			model.addAttribute("title", "warning");
		}
		model.addAttribute("loc", "/inquery/view?check=1&inqueryNo=" + ic.getInqueryRef());
		return "common/msg";
	}
	
	@GetMapping(value = "/deleteComment")
	public String deleteComment(InqueryComment ic, Model model) {
		int result = inqueryService.deleteComment(ic);
		if (result > 0) {
			model.addAttribute("title", "삭제 성공");
			model.addAttribute("msg", "댓글이 삭제되었습니다!");
			model.addAttribute("icon", "success");
		} else {
			model.addAttribute("title", "삭제 실패");
			model.addAttribute("title", "잠시 후 다시 시도해주세요..");
			model.addAttribute("title", "warning");
		}
		model.addAttribute("loc", "/inquery/view?check=1&inqueryNo=" + ic.getInqueryRef());
		return "common/msg";
	}
	
}







































