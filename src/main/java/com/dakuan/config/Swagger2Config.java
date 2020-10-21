package com.dakuan.config;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 // 启用Swagger2
public class Swagger2Config {

    @Value("${swagger.title}")
    private String title;
    @Value("${swagger.description}")
    private String description;
    @Value("${swagger.version}")
    private String version;

    @Configuration
    public class Swagger2 {

        @Bean
        public Docket createRestApi() {// 创建API基本信息
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .ignoredParameterTypes(JSONObject.class, JSONArray.class)
                    .select()
                    .apis(RequestHandlerSelectors.any())
                    .paths(PathSelectors.any())
                    .build();

        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title(title) //访问地址：http://localhost:1188/swagger-ui.html
                    .description(description)
                    .version(version)
                    .build();
        }
    }


}
