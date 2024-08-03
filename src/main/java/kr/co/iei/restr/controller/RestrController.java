package kr.co.iei.restr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.iei.restr.model.dto.Restaurant;
import kr.co.iei.restr.model.service.RestrService;

@Controller
@RequestMapping(value = "/restaurant")
public class RestrController {
	@Autowired
	private RestrService restrService;
	
	@Value("${file.root}")
	private String root; //application.properties에 설정되어있는 file.root값을 가지고와서 문자열로 지정

	@GetMapping(value = "/restrView")
	public String restrView(Model model) {
		Restaurant r = restrService.selectOneRestr(1);
		if(r == null) {
			return "/";
		} else {			
			model.addAttribute("r",r);
			return "restaurant/restrView";
		}
	}
	
	@GetMapping(value="/writeFrm")
	public String writeFrm() {
		return "restaurant/restrWriteFrm";
	}//restFrm()
	
	@GetMapping(value="/updateFrm")
	public String updateFrm() {
		return "restaurant/restrUpdateFrm";
	}//restFrm()
}
