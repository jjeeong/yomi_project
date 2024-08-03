package kr.co.iei.util;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.iei.member.model.dto.Member;

public class LoginInsterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//컨트롤러쪽으로 들어가는 요청을 가로챔
		//-> return타입이 boolean -> true를 리턴하면 컨트롤러 실행 / false를 리턴하면 컨트롤러 실행 X
		// 로그인 여부를 체크해서 로그인 되어있으면 Controller를 수행
		//							로그인 되어있지않으면 로그인 알림 후 로그인페이지로 이동
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("member");
		if(member != null) {
			return true;
		} else {
			//페이지 이동
			response.sendRedirect("/member/loginMsg");
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//컨트롤러 수행 후 응답을 가로챔
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 인터셉터 수행이 끝나면 무조건 실행
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
}
