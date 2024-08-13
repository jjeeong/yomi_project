package kr.co.iei;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import kr.co.iei.util.AdminInterceptor;
import kr.co.iei.util.LoginInsterceptor;

//스프링부트 설정파일
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
	@Value("${file.root}")
	private String root;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/**")		 //별 두개 : 안에 폴더가 몇개있든 모두
			.addResourceLocations("classpath:/templates", "classpath:/static/");
		registry
			.addResourceHandler("/yomi/**")
			.addResourceLocations("file:///"+root+"/yomi/");
		registry
			.addResourceHandler("/notice/editor/**")
			.addResourceLocations("file:///"+root+"/notice/editor/");
		registry
			.addResourceHandler("/board/thumbNailImg/**")
			.addResourceLocations("file:///"+root+"/board/thumbNailImg/");
		registry
			.addResourceHandler("/board/editor/**")
			.addResourceLocations("file:///"+root+"/board/editor/");
		registry
			.addResourceHandler("/review/**")
			.addResourceLocations("file:///"+root+"/review/");
		registry
			.addResourceHandler("/inquery/**")
			.addResourceLocations("file:///"+root+"/inquery/");
		registry
			.addResourceHandler("/inquerySummernote/**")
			.addResourceLocations("file:///"+root+"/inquerySummernote/");
		registry
		.addResourceHandler("/notice/**")
		.addResourceLocations("file:///"+root+"/notice/");
		registry
			.addResourceHandler("/noticeSummernote/**")
			.addResourceLocations("file:///"+root+"/noticeSummernote/");
		}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInsterceptor()) // 사용할 Interceptor 객체? class? 넣기
				.addPathPatterns("/member/logout",
								"/notice/**",	//notice안에 있는 것 다↓
								"/admin/**",	//두개 이상의 인터셉터의 우선순위: 먼저쓴게 먼저 동작
								"/board/**",
								"/restaurant/**",
								"/report/**"
								)
				.excludePathPatterns("/notice/list",//excludePathPatterns : 예외항목(넣을게 많은 경우 여집합이용)
									"/notice/view", 
									"/notice/filedown",
									"/board/list",
									"/board/view",
									"/review/**",
									"/board/thumbNailImg/**",
									"/inquery/**",
									"/inquerySummernote/**",
									"/board/editor/**",
									"/notice/editor/**", //주의할점: notice/**에는 controller뿐 아니라 다른 사진이든 뭐든 다 걸리므로 예외처리할때 주의할것
									"/restaurant/restrView",
									"/restaurant/countTagList",
									"/restaurant/restrList",
									"/restaurant/more",
									"/restaurant/restrSearch",
									"/restaurant/reviewMore",
									"/restaurant/reviewList",
									"/restaurant/writeReviewFrm",
									"/restaurant/likePush",
									"/restaurant/bookmarkPush",
									"/restaurant/reviewLikePush"
									);
		registry.addInterceptor(new AdminInterceptor())
				.addPathPatterns("/admin/**",
								 "/notice/**",
								 "/restaurant/writeFrm",
								 "/restaurant/restrWrite",
								 "/restaurant/updateFrm",
								 "/restaurant/restrUpdate",
								 "/restaurant/deleteRestr"
						)
				.excludePathPatterns("/admin/adminMypage",
									"/admin/updateMyPage",
									"/admin/update2",
									"/admin/myposting",
									"/admin/myreviews",
									"/notice/list",
									"/notice/view",
									"/notice/filedown"
									
									
									);
	}
}
