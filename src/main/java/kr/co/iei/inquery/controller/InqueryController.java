package kr.co.iei.inquery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.iei.inquery.model.dto.InqueryListData;
import kr.co.iei.inquery.model.service.InqueryService;

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
}














