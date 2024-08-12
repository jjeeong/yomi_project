package kr.co.iei.notice.controller;

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
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.iei.inquery.model.dto.Inquery;
import kr.co.iei.inquery.model.dto.InqueryFile;
import kr.co.iei.member.model.dto.Member;
import kr.co.iei.notice.model.dto.Notice;
import kr.co.iei.notice.model.dto.NoticeFile;
import kr.co.iei.notice.model.dto.NoticeListData;
import kr.co.iei.notice.model.service.NoticeService;
import kr.co.iei.util.FileUtils;

@Controller
@RequestMapping(value="/notice")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Value("${file.root}")
	private String root;
	
	@GetMapping(value="/list")
	public String list(int reqPage,Model model) {
		NoticeListData nld = noticeService.selectNoticeList(reqPage);
		model.addAttribute("list",nld.getList());
		model.addAttribute("pageNavi",nld.getPageNavi());
		return "notice/list";
	}
	@GetMapping(value = "/editorFrm")
	public String editorFrm(@SessionAttribute(required = false) Member member) {		
		return "notice/editorFrm";
	}
	@ResponseBody
	@PostMapping(value = "/editorImage", produces = "text/plain;charset=utf-8")
	public String editorImage(MultipartFile upfile) {
		String savepath = root + "/noticeSummernote/";
		String filepath = fileUtils.upload(savepath, upfile);
		return "/noticeSummernote/" + filepath;
	}
	
	@PostMapping(value = "/write")
	public String write(Notice n, MultipartFile[] upfile, Model model) {
		
		List<NoticeFile> fileList = new ArrayList<NoticeFile>();
		
		if(!upfile[0].isEmpty()) {
			//C:/Temp/upload/inquery/
			String savepath = root + "/notice/";
			for(MultipartFile file : upfile) {
				String filename = file.getOriginalFilename();
				String filepath = fileUtils.upload(savepath, file);
				NoticeFile noticeFile = new NoticeFile();
				noticeFile.setFileName(filename);
				noticeFile.setFilePath(filepath);
				fileList.add(noticeFile);
			}
		}
		// inq : inqueryTile.inqueryWriter, inqueryContent
		// fileList : (InqueryFile) x 첨부파일갯수
		int result = noticeService.insertnotice(n,fileList);
		if(result>0) {
			model.addAttribute("title","작성성공!");
			model.addAttribute("msg","공지사항 작성에 성공했습니다.");
			model.addAttribute("icon","success");
			model.addAttribute("loc","/notice/list?reqPage=1");
			return "common/msg";
		}
		return "redirect:/notice/editorFrm";
	}
	@GetMapping(value = "/view")
	public String view(int noticeNo, String check, Model model, @SessionAttribute(required = false) Member member) {
		int memberNo = 0;
		if(member != null) {
			memberNo = member.getMemberNo();
		}
		System.out.println(noticeNo);
		System.out.println(check);
		//로그인이 되어있지않으면 memberNo = 0 / 로그인이 되어있으면 memberNo = 로그인한 회원번호
		Notice n = noticeService.selectOneNotice(noticeNo,check,memberNo);
		System.out.println("n"+n);
		//System.out.println("inq:"+inq);
		if(n == null) {
			model.addAttribute("title","조회실패");
			model.addAttribute("msg","해당 게시글이 존재하지 않습니다.");
			model.addAttribute("icon","info");
			model.addAttribute("loc","/notice/list?regPage=1");
			return "common/msg";
		}else {
			model.addAttribute("notice", n);		
			return "notice/view";
		}		
	}
	@GetMapping(value = "/filedown")
	public void filedown(NoticeFile noticeFile, HttpServletResponse response) {
		String savepath = root + "/notice/";
		fileUtils.downloadFile(savepath, noticeFile.getFileName(), noticeFile.getFilePath(),response);
	}
	
	@GetMapping(value = "/delete")
	public String delete(int noticeNo, Model model) {
		// 삭제를 하게되면 해당 게시글의 첨부파일도 삭제 -> 삭제결과로 파일 목록을 가져옴
		List<NoticeFile> list = noticeService.deleteNotice(noticeNo);
		if(list == null) {
			model.addAttribute("title", "삭제 실패");
			model.addAttribute("msg", "해당 게시글이 존재하지 않습니다..");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/notice/list?reqPage=1");
		}else {
			String savepath = root + "/notice/";
			for (NoticeFile file : list) {
				File delFile = new File(savepath + file.getFilePath());
				delFile.delete();
			}
			model.addAttribute("title", "삭제 성공");
			model.addAttribute("msg", "게시글이 삭제되었습니다..");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/notice/list?reqPage=1");
		}
		return "common/msg";		
	}
	
	@GetMapping(value = "/updateFrm")
	public String updateFrm(int noticeNo, Model model) {
		Notice n = noticeService.getOneNotice(noticeNo);
		model.addAttribute("notice",n);
		System.out.println("n"+n);
		return "notice/updateFrm";
	}
	
	@PostMapping(value = "/update")
	public String update(Notice n, MultipartFile[] upfile, int[] delFileNo, Model model) {
		// 새로 추가된 파일 업로드 작업
		//System.out.println("no"+inq.getInqueryNo());
		//System.out.println("upfile:"+upfile);
		List<NoticeFile> fileList = new ArrayList<NoticeFile>();
		String savepath = root + "/notice/";
		if(!upfile[0].isEmpty()) {
			for(MultipartFile file : upfile) {
				String fileName = file.getOriginalFilename();
				String filePath = fileUtils.upload(savepath,file);
				NoticeFile noticeFile = new NoticeFile();
				noticeFile.setFileName(fileName);
				noticeFile.setFilePath(filePath);
				noticeFile.setNoticeNo(n.getNoticeNo());
				fileList.add(noticeFile);
			}
		}
		//System.out.println("fileList:"+fileList);
		// 수정요청하면서 데이터 3개전달 (n : inquery테이블 수정, fileList : inquery_file추가, delFileNo : inquery_file 삭제용)
		List<NoticeFile> delFileList = noticeService.updateNotice(n, fileList, delFileNo);
		if(delFileList == null) {
			model.addAttribute("title", "수정 실패");
			model.addAttribute("msg", "처리중 문제가 발생했습니다.. 잠시후 다시 시도해주세요..");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/notice/list?reqPage=1");
			return "common/msg";
		}else {
			for (NoticeFile noticeFile : delFileList) {
				File delFile = new File(savepath + noticeFile.getFilePath());
				delFile.delete();
			}
			return "redirect:/notice/view?noticeNo=" + n.getNoticeNo();
		}
	}
}
