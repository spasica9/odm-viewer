package com.example.odm_viewer_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.odm_viewer_backend", "controller" })
public class OdmViewerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OdmViewerBackendApplication.class, args);
	}

}
