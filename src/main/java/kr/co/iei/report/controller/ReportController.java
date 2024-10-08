package kr.co.iei.report.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.iei.member.model.dto.Member;
import kr.co.iei.member.model.service.MemberService;
import kr.co.iei.report.model.dto.RP;
import kr.co.iei.report.model.dto.Report;
import kr.co.iei.report.model.service.ReportService;

@Controller
@RequestMapping(value="/report")
public class ReportController {
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private MemberService memberService;
	
	
	@GetMapping(value="/insertReviewReport")
	@ResponseBody
	public int insertReviewReport(@SessionAttribute Member member,int reportBoardType, int reportReviewNo, int respondentNo, String reportType, String reportContent) {
		Report r = new Report();
		r.setReporterNo(member.getMemberNo());
		r.setReportReviewNo(reportReviewNo);
		r.setReportBoardType(reportBoardType);
		int count = reportService.checkDuplication(r);
		if(count == -2) {
			return -2;
		}
		if(member.getMemberNo()==respondentNo) {
			return -1;
		}else {
			r.setRespondentNo(respondentNo);
			r.setReportType(reportType);
			r.setReportContent(reportContent);
			int result = reportService.insertReviewReport(r);
			return result;			
		}
	}
	
	@GetMapping(value="/insertOtherReport")
	@ResponseBody
	public int insertOtherReport(@SessionAttribute Member member,int reportBoardType, int reportContentNo, String respondentId, String reportType, String reportContent) {
		System.out.println(respondentId);
		if(member.getMemberId().equals(respondentId)) {
			return -1;
		}else {
			Member m = memberService.selectOneMember(respondentId);
			Report r = new Report();
			r.setReportBoardType(reportBoardType);
			r.setReporterNo(member.getMemberNo());
			r.setReportContent(reportContent);
			r.setReportType(reportType);
			r.setRespondentNo(m.getMemberNo());
			int result=0;
			int count=0;
			switch(reportBoardType) {
			case 2:
				r.setReportBoardNo(reportContentNo);
				count = reportService.checkDuplication(r);
				//System.out.println(r);
				if(count ==-2) {
					return -2;
				}
				result = reportService.insertBoardReport(r);
				break;
			case 3:
				r.setReportBoardCommentNo(reportContentNo);
				//System.out.println(r);
				count = reportService.checkDuplication(r);
				if(count ==-2) {
					return -2;
				}
				result = reportService.insertBoardCommentReport(r);
				break;
			}
			return result;			
		}
	}
	
	@GetMapping(value="/checkReport")
	public String checkReport(Model model, int reqPage) {
		RP r = reportService.selectUncheckReport(reqPage);
		model.addAttribute("list", r.getList());
		model.addAttribute("pagiNavi", r.getPagiNavi());
		return "report/reportList";
	}
	
	@ResponseBody
	@GetMapping(value="/showReport")
	public Report showReport(int reportNo) {
		Report r = reportService.selectOneReport(reportNo);
		return r;
	}
	
	
	@GetMapping(value="/updateReport")
	public String updateReport(int[] reportNo, Model model) {
		int result = reportService.updateReportCheck(reportNo);
		if(result>0) {
			model.addAttribute("title" , "신고 체크 완료");
			model.addAttribute("text", "신고 횟수가 정상적으로 집계되었습니다.");
			model.addAttribute("icon", "success");
		}else {
			model.addAttribute("title" , "신고 체크 실패");
			model.addAttribute("text", "체크할 신고글이 존재하지 않습니다.");
			model.addAttribute("icon", "error");
		}
		model.addAttribute("loc", "/report/checkReport?reqPage=1");
		return "common/msg2";
	}
	
	
	@GetMapping(value="/deleteReport")
	public String deleteReport(int[] reportNo, Model model) {
		int result = reportService.deleteReport(reportNo);
		if(result>0) {
			model.addAttribute("title" , "신고 삭제 완료");
			model.addAttribute("text", "신고 내역이 정상적으로 삭제 되었습니다.");
			model.addAttribute("icon", "success");
		}else {
			model.addAttribute("title" , "신고 삭제 실패");
			model.addAttribute("text", "삭제할 신고글이 존재하지 않습니다.");
			model.addAttribute("icon", "error");
		}
		model.addAttribute("loc", "/report/checkReport?reqPage=1");
		return "common/msg2";
	}
	
	@GetMapping(value="/searchByReportType")
	public String searchByReportType(int reportType, Model model, int reqPage) {
		String[] reportTypeArr = {"스팸 홍보/도배","음란물","불법 정보 포함","불쾌한 내용 포함","잘못된 정보 포함"};
		if(reportType == 6) {
			RP r = reportService.searchByReportTypeETC(reqPage);
			model.addAttribute("list", r.getList());
			model.addAttribute("pagiNavi", r.getPagiNavi());
		}else {
			RP r = reportService.searchByReportType(reportTypeArr[reportType-1], reqPage, reportType);
			model.addAttribute("list", r.getList());
			model.addAttribute("pagiNavi", r.getPagiNavi());
		}
		return "report/reportList";
	}//searchByReportType
	
	@GetMapping(value="/searchByBoardType")
	public String searchByBoardType(int reportBoardType,int reqPage, Model model) {
		RP r = reportService.searchByBoardType(reportBoardType, reqPage);
		model.addAttribute("list", r.getList());
		model.addAttribute("pagiNavi", r.getPagiNavi());
		return "report/reportList";
	}//searchByReportType
	
	@GetMapping(value="/searchById")
	public String searchById(String respondentId,int reqPage, Model model) {
		RP r = reportService.searchById(respondentId, reqPage);
		model.addAttribute("list", r.getList());
		model.addAttribute("pagiNavi", r.getPagiNavi());
		return "report/reportList";
	}
}
