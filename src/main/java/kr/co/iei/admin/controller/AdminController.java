package kr.co.iei.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.iei.admin.dto.AdminListData;
import kr.co.iei.admin.service.AdminService;
import kr.co.iei.member.model.dto.Member;
import kr.co.iei.member.model.service.MemberService;
import kr.co.iei.report.model.service.ReportService;
import kr.co.iei.restr.model.service.RestrService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	@Autowired
	private ReportService reportService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private RestrService restrService;

	@GetMapping(value = "/checkReport")
	public String checkReport() {
		return "admin/reportList";

	}

	
	@GetMapping(value="/adminMypage")
	public String adminMypage(@SessionAttribute Member member,Model model) {

		String memberId = member.getMemberId();
		int memberNo = member.getMemberNo();
		Member m = memberService.selectOneMember(memberId);
		List list = restrService.selectListRestr(memberNo);
		model.addAttribute("m", m);
		model.addAttribute("rs",list);
		return "admin/adminMypage";
	}


	@GetMapping(value = "/adminPage")
	public String adminPage(Model model, int reqPage) {

		AdminListData ald = adminService.selectAdminList(reqPage);
		model.addAttribute("list", ald.getList());
		model.addAttribute("pageNavi", ald.getPageNavi());

	
		return "admin/adminPage";
	}

	@GetMapping(value = "/changeGrade")
	public String changeGrade(Member m, Model model) {
		int result = adminService.changeGrade(m);
		if (result > 0) {
			return "redirect:/admin/adminPage?reqPage=1";
		} else {
			model.addAttribute("title", "등급 변경 실패");
			model.addAttribute("msg", "개발자에게 문의하세요");
			model.addAttribute("icon", "warning");
			model.addAttribute("loc", "/admin/adminPage?reqPage=1");
			return "common/msg";

		}
	}

	@GetMapping(value = "/checkedChangeGrade")
	public String checkedChangeGrade(String no, String grade, Model model) {
		boolean result = adminService.checkedChangeGrade(no, grade);
		if (result) {
			return "redirect:/admin/adminPage";
		} else {
			model.addAttribute("title", "등급 변경 실패");
			model.addAttribute("msg", "개발자에게 문의하세요");
			model.addAttribute("icon", "warning");
			model.addAttribute("loc", "/admin/adminPage");
			return "common/msg";
		}

	}

	@GetMapping(value = "/blackPage")
	public String blackPage(Model model) {
		List blackPage = adminService.selectAllMember();
		model.addAttribute("list", blackPage);
		return "admin/blackPage";
	}
	
	@GetMapping(value="/delete")
	public String deleteMember(@SessionAttribute Member member , Model model) {
		int memberNo = member.getMemberNo();
		int result = adminService.deleteMember(memberNo);
		if(result > 0) {
			model.addAttribute("title","탈퇴완료");
			model.addAttribute("msg","안녕히가세요");
			model.addAttribute("icon","success");
			model.addAttribute("loc","/member/logout");		
		}else {
			model.addAttribute("title","탈퇴실패");
			model.addAttribute("msg","처리 중 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
			model.addAttribute("icon","error");
			model.addAttribute("loc","/admin/adminMypage");		
		}
		return "common/msg";	
    }
	@GetMapping(value="/updateMypage")
	public String updateMypage(@SessionAttribute Member member, Model model) {		
		String memberId = member.getMemberId();
		Member m = memberService.selectOneMember(memberId);
		model.addAttribute("m", m);
		return "admin/updateMypage";
	}
	@PostMapping(value="/update2")
	public String update2(Member m, @SessionAttribute Member member) {
		int result = adminService.updateMember(m);
		if(result > 0) {
			//정보변경에 성공한 경우 -> 세션의 데이터를 갱신 => 최신화
			member.setMemberPw(m.getMemberPw());
			member.setMemberAddr(m.getMemberAddr());
			member.setMemberPhone(m.getMemberPhone());			
			return "redirect:/admin/updateMypage";
		}else {
			return "redirect:/";
		}
	}	
	@GetMapping(value="/myposting")
	public String myposting(@SessionAttribute Member member, Model model) {
		String memberId = member.getMemberId();
		List myposting = adminService.postingMember(memberId);
		System.out.println(myposting);
		model.addAttribute("p",myposting);
		return "admin/myposting";			
	}	
	@GetMapping(value="/myreviews")
	public String myreviews(@SessionAttribute Member member, Model model) {
		int memberNo = member.getMemberNo();
		List myreviews = adminService.reviewsMember(memberNo);
		model.addAttribute("v",myreviews);
		return "admin/myreviews";
	}	
}