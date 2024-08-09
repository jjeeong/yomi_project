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
import kr.co.iei.report.model.dto.Report;
import kr.co.iei.report.model.service.ReportService;

@Controller
@RequestMapping(value="/report")
public class ReportController {
	@Autowired
	private ReportService reportService;
	
	@GetMapping(value="/reportModal")
	public String reportModal() {
		return "report/checkReportInsert";
	}// 디자인 확인용(나중에 지우든 수정하든 할것)
	
	@GetMapping(value="/insertReviewReport")
	@ResponseBody
	public int insertReviewReport(@SessionAttribute Member member,int reportBoardType, int reportReviewNo, int respondentNo, String reportType, String reportContent) {
		if(member.getMemberNo()==respondentNo) {
			return -1;
		}else {
			Report r = new Report();
			r.setReportBoardType(reportBoardType);
			r.setReportContent(reportContent);
			r.setReporterNo(member.getMemberNo());
			r.setReportReviewNo(reportReviewNo);
			r.setReportType(reportType);
			r.setRespondentNo(respondentNo);
			int result = reportService.insertReviewReport(r);
			return result;			
		}
	}
	
	@GetMapping(value="/checkReport")
	public String checkReport(Model model) {
		List<Report> list = reportService.selectUncheckReport();
		model.addAttribute("list", list);
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
		model.addAttribute("loc", "/report/checkReport");
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
		model.addAttribute("loc", "/report/checkReport");
		return "common/msg2";
	}
}
