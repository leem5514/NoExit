// package com.E1i3.NoExit.domain.chat.config;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// @Configuration
// public class CorsConfig implements WebMvcConfigurer {
// // 이거는 원래 WebSocketConfig에서 받는 걸로 아는데 왜 적용이 안될까?
//
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOrigins("*")
//                 .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
//                 .allowedHeaders("Authorization", "Content-Type")
//                 .maxAge(3600);
//     }
//
// }
