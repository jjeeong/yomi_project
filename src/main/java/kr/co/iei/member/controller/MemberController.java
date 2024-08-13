package kr.co.iei.member.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
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
import kr.co.iei.util.EmailSender;

@Controller
@RequestMapping(value="/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private EmailSender emailSender;
	
	@GetMapping(value="/loginFrm")
	public String loginFrm() {
		return "member/login";
	}
	
	@PostMapping(value="/login")
	public String login(Member m,HttpSession session,Model model) {
		Member member = memberService.selectOneMember(m);
		if(member == null) {
			model.addAttribute("member", "no");
			return "/member/login";
		}else {
			session.setAttribute("member", member);
			return "redirect:/";
		}
		
		
		
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
	
	@GetMapping(value="/findIdFrm")
	public String findIdFrm() {
		return "/member/findId";
	}
		
	@PostMapping(value="/findId")
	public String findId(Member m,Model model) {
		Member member = memberService.findId(m);
		model.addAttribute("member" , member);
		if(member != null) {
			return "/member/findIdSelect";
		}else {
			return "/member/null";
		}
	}
	
	@GetMapping(value="/findPwFrm")
	public String findPwFrm() {
		return "/member/findPw";
	}	
	
	@PostMapping(value="/findPwUpdate")
	public String findPwUpdate(Member m,Model model) {
		Member member = memberService.findName(m);
		model.addAttribute("member", member);
		if(member != null) {
			return "/member/PwUpdateFrm";
		}else {
			return "/member/null";
		}
	}
	@PostMapping(value="/updatePw")
	public String updatePw(Member m,Model model) {
		int result = memberService.updatePw(m);
		if(result>0) {
			model.addAttribute("title","비밀번호 변경 성공");
			model.addAttribute("msg","비밀번호 변경이 완료됐습니다.");
			model.addAttribute("icon","success");
			model.addAttribute("loc","/");
			return "common/msg";
		}else {
			model.addAttribute("title","비밀번호 변경실패");
			model.addAttribute("msg","관리자에게 문의하세요.");
			model.addAttribute("icon","warning");
			model.addAttribute("loc","/member/loginFrm");
			return "common/msg";
		}
	}
	
	@ResponseBody
	@PostMapping(value="/sendCode")
	public String sendCode(String receiver) {
		//인증메일 제목 생성
		String emailTitle = "YOMIYOMI 인증메일입니다.";
		//인증메일 인증코드 생성
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<6;i++) {
			//0 ~ 9 : r.nextInt(10);
			//A ~ Z : r.nextInt(26)+65;
			//a ~ z : r.nextInt(26)+97;
			int flag = r.nextInt(3); //0,1,2 -> 숫자쓸지, 대문자 쓸지, 소문자쓸지 결정
			if(flag == 0) {
				int randomCode = r.nextInt(10);
				sb.append(randomCode);
			}else if(flag == 1) {
				char randomCode = (char)(r.nextInt(26)+65);
				sb.append(randomCode);
			}else if(flag == 2) {
				char randomCode = (char)(r.nextInt(26)+97);
				sb.append(randomCode);
			}
		}
		String emailContent = "<h1>안녕하세요. YOMIYOMI 입니다.</h1>"
							+"<h3>본인인증을 위한 인증번호는 [<span style='color:red'>"
							+sb.toString()
							+"</span>]입니다.</h3>";
		emailSender.sendMail(emailTitle, receiver, emailContent);
		System.out.println("cot"+emailTitle);
		System.out.println("cot"+receiver);
		System.out.println("cot"+emailContent);
		return sb.toString();
		
	}

}
