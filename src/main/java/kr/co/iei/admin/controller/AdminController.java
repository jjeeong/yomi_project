package kr.co.iei.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.iei.report.model.service.ReportService;

@Controller
@RequestMapping(value="/admin")
public class AdminController {
	@Autowired
	private ReportService reportService;
	
	@GetMapping(value="/checkReport")
	public String checkReport() {
		return "admin/reportList";
	}
}
