//package com.E1i3.NoExit.domain.common;
//
//import io.swagger.annotations.Api;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.bind.annotation.GetMapping;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@EnableSwagger2 // 나중에 스웨거 쓰면 걍 이거 가져다 쓰면 되겟지?
//@Configuration
//
////의존성만 추가했고 수정이 필요함
//public class SwaggerConfig {
//
//    @Bean // Bean이 있으면 Configuration이 있어야한다.
////  Docket : Swagger 구성의 핵심 기능 클래스
////  Docket을 리턴함으로써 싱글톤 객체로 생성한다.
//
//    @GetMapping("/member/text")
//    public String text(){
//        return "ok";
//    }
//
//    public Docket api(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.ant("/rest/**")) // *의 의미는? * : 직계 경로 즉 바로 아래 경로, ** : 자손까지 포함
//                .build();
//
//    }
//
//}
