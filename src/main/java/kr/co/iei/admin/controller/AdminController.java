package kr.co.iei.admin.controller;

import java.util.List;	

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.iei.member.model.dto.Member;
import kr.co.iei.member.model.service.MemberService;
import kr.co.iei.report.model.service.ReportService;

@Controller
@RequestMapping(value="/admin")
public class AdminController {
	@Autowired
	private ReportService reportService;
	@Autowired
	private MemberService memberService;
	
	
	@GetMapping(value="/checkReport")
	public String checkReport() {
		return "admin/reportList";
		
	}
	@GetMapping(value="/adminMypage")
	public String adminMypage(@SessionAttribute Member member,Model model) {
		String memberId = member.getMemberId();
		Member m = memberService.selectOneMember(memberId);
		model.addAttribute("m", m);
		return "admin/adminMypage";
	}
	@GetMapping(value="/adminPage")
	public String admin_page(Model model) {
		//List list = memberService.selectAllMember();
		//model.addAttribute("list", list);
		return "admin/adminPage";
		 
	}
	
}
