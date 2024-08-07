package kr.co.iei.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
}
