package com.LostFound.MainProject.Config;



import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/**")
	            .allowedOrigins("http://localhost:4200")
	            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	            .allowedHeaders("*")
	            .allowCredentials(true);
	}



    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve all /uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + Paths.get("uploads").toAbsolutePath() + "/");

        // Serve all /images/** from both lost and found folders
        registry.addResourceHandler("/images/**")
                .addResourceLocations(
                    "file:" + Paths.get("uploads/lost-images").toAbsolutePath() + "/",
                    "file:" + Paths.get("uploads/found-images").toAbsolutePath() + "/"
                );
    }



}
