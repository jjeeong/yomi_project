package kr.co.iei.inquery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.iei.inquery.model.dto.InqueryListData;
import kr.co.iei.inquery.model.service.InqueryService;
import kr.co.iei.member.model.dto.Member;

@Controller
@RequestMapping(value = "/inquery")
public class InqueryController {
	@Autowired
	private InqueryService inqueryService;
	
	
	
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
		return "notice/editorFrm";
	}
	
}














