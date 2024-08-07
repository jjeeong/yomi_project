package kr.co.iei;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

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
			.addResourceHandler("/review/**")
			.addResourceLocations("file:///"+root+"/review/");
		registry
			.addResourceHandler("/board/editor/**")
			.addResourceLocations("file:///"+root+"/board/editor/");
	}
}
