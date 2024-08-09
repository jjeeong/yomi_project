package kr.co.iei.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

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
	

	@PostMapping(value="/join")
	public String join(Member m,Model model) {
		int result = memberService.insertMember(m);
		if(result>0) {
			model.addAttribute("title","회원가입성공");
			model.addAttribute("msg","회원가입이 완료됐습니다.");
			model.addAttribute("icon","success");
			model.addAttribute("loc","/");
			return "common/msg";
		}else {
			model.addAttribute("title","회원가입실패");
			model.addAttribute("msg","관리자에게 문의하세요.");
			model.addAttribute("icon","warning");
			model.addAttribute("loc","/member/join");
			return "common/msg";
		}
		
		
		
		
		
	}
	@ResponseBody
	@GetMapping(value="/ajaxCheckId")
	public int selectOneMemberId(String memberId) {
		Member member = memberService.selectOneMemberId(memberId);
		if(member == null) {
			return 0;
		}else {
			return 1;
		}
	}
	@ResponseBody
	@GetMapping(value="/ajaxCheckEmail")
	public int selectOneMemberEmail(String email) {
		System.out.println(email);
		Member member = memberService.selectOneMemberEmail(email);
		if(member == null) {
			return 0;
		}else {
			return 1;
		}
	}

}
