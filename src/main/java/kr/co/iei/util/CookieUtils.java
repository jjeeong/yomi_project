package kr.co.iei.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtils {
	//쿠키 키값을 주고 value값 받는 메소드...
	public String getCookieValue(HttpServletRequest request, String cookiename) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(cookiename)) {
					return cookie.getValue();
				}//if
			}//for
		}//if
		return null;
	}
	//쿠키값 생성
	public void setCookie(HttpServletRequest request, HttpServletResponse response,String cookieName, String cookieValue) {
		cookieName = URLEncoder.encode(cookieName, StandardCharsets.UTF_8);//invalid[32] 인코딩필요>>img파일 이름은 이것도 안되나봄..
		String currentCookieVal = getCookieValue(request, cookieName);
		String[] cookievalues;
		try {
			if(currentCookieVal != null) {
				cookievalues=currentCookieVal.split("/"); //톰캣 8,9는 쉼표를 지원안한다고 함[44]invalid + 스페이스바도 지원안함 그러면 상호명도 자바에서는 안되겠네..
				//중복 체크를 해야함..
				for(int i=0;i<cookievalues.length;i++) {
					if(cookievalues[i].equals(cookieValue)) {
						return;
					}
				}
				if(cookievalues.length>=3) {
					for(int i=0; i<cookievalues.length; i++) {
						if(i<2) {
							cookievalues[i]=cookievalues[i+1];
						}else {
							cookievalues[i]=null;
						}//if/else
					}//for
					cookievalues[2] = cookieValue;
				}//if 길이가 3보다 크면
				else {
					cookievalues = Arrays.copyOf(cookievalues, cookievalues.length+1);
					cookievalues[cookievalues.length-1] = cookieValue;
				}
				String newCookieValue = String.join("/", cookievalues);
				Cookie cookie = new Cookie(cookieName, newCookieValue);
				cookie.setMaxAge(60*60*24);
				cookie.setPath("/");
				response.addCookie(cookie);
				System.out.println(cookie.getValue());
				
			}else {
				Cookie cookie = new Cookie(cookieName, cookieValue);
				cookie.setMaxAge(60*60*24);
				cookie.setPath("/");
				response.addCookie(cookie);
				System.out.println(cookie.getValue());
			}
		} catch (IllegalArgumentException e) {
			System.out.println(cookieName + cookieValue);//혹시 오류나면..해당 쿠키 키값이랑 value값 받아오게..>이걸로 restrName, restrImg1이 오류난다는걸 깨달음
		}
		
		
	}
}
