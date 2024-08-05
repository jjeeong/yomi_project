package kr.co.iei;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
import kr.co.iei.util.AdminInterceptor;
import kr.co.iei.util.LoginInterceptor;
*/

//스트링부트 설정파일
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
	@Value("${file.root}")
	private String root;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		//안에 폴더가 몇개있던 다가져와 **
			.addResourceHandler("/**")
			.addResourceLocations("/classpath:/templates/", "classpath:/static/");
				
		registry
			.addResourceHandler("/photo/**")
			.addResourceLocations("file:///"+root+"/photo/");
		
		
		registry
			.addResourceHandler("/board/editor/**")
			.addResourceLocations("file:///"+root+"/board/editor/");
	}
}
	/*
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor())
				.addPathPatterns("/member/logout",
								"/member/mypage1",
								"/member/update1",
								"/member/update2",
								"/member/mypage2",
								"/member/delete",
								"/notice/**",
								"/admin/**")
				.excludePathPatterns("/notice/list","/notice/view","/notice/filedown","/notice/editor/**");
		
		registry.addInterceptor(new AdminInterceptor())
				.addPathPatterns("/admin/**");
	}
}
*/