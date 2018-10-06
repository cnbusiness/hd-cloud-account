package com.hd.cloud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * 
 * @ClassName: SwaggerConfig
 * @Description: Swagger Config
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月21日 下午3:06:06
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket createRestApi() {// 创建API基本信息
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.hd.cloud.rest"))// 扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {// 创建API的基本信息，这些信息会在Swagger UI中进行显示
		return new ApiInfoBuilder().title("Hd Cloud Account RESTful APIs")// API 标题
				.description("Account RESTful APIs")// API描述
				.version("1.1")// 版本号
				.build();
	}
}
