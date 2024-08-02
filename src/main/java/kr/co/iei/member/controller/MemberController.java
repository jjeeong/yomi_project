package kr.co.iei.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/member")
public class MemberController {
	
	
	@GetMapping(value="/loginFrm")
	public String loginFrm() {
		return "member/login";
	}
}
