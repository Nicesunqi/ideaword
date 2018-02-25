package com.graphics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

import com.graphics.common.filter.DifferentDomainFilter;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@ImportResource("classpath:spring/spring.xml")
public class Application {

	@Autowired
	private Environment env;

	/**
	 * 配置跨越访问filter
	 */
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();

		DifferentDomainFilter differentDomainFilter = new DifferentDomainFilter();
		registrationBean.setFilter(differentDomainFilter);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");
		registrationBean.setUrlPatterns(urlPatterns);

		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setAdditionalProfiles();
		app.setBannerMode(Banner.Mode.LOG);
		app.run(args);
	}

}
