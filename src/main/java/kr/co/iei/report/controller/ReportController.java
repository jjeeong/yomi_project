package kr.co.iei.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
