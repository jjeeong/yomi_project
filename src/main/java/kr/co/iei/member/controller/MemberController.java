package kr.co.iei.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.http.HttpSession;
import kr.co.iei.member.model.dto.Member;
import kr.co.iei.member.model.service.MemberService;

@Controller
@RequestMapping(value="/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	
	@GetMapping(value="/loginFrm")
	public String loginFrm() {
		return "member/login";
	}
	
	@PostMapping(value="/login")
	public String login(Member m,HttpSession session) {
		System.out.println(m.getMemberId());
		System.out.println(m.getMemberPw());
		Member member = memberService.selectOneMember(m);
		
		session.setAttribute("member", member);
		System.out.println(member);
		return "redirect:/";
	}
	@GetMapping(value="/logout")
	public String logout(HttpSession session) {
		session.invalidate(); //현재 세션 정보를 파기
		return "redirect:/";
	}
	
	@GetMapping(value="/joinFrm")
	public String joinFrm() {
		return "member/joinFrm";
	}
	
}
