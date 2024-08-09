package kr.co.iei.util;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.iei.member.model.dto.Member;

public class AdminInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Member m = (Member)session.getAttribute("member");
		if(m.getMemberGrade()==1) {
			return true;
		}else {
			response.sendRedirect("/member/adminMsg");
			return false;
		}
	}
	
}
