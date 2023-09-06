package com.example.compressimage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Administrator
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class CompressImageApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompressImageApplication.class, args);
	}
}
