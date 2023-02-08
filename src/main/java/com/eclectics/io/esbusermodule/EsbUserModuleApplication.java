package com.eclectics.io.esbusermodule;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EsbUserModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsbUserModuleApplication.class, args);
	}

	@Bean
	public Gson provideGson(){
		return new Gson ();
	}

	@Bean
	public PasswordEncoder providesEncoder(){
		return new BCryptPasswordEncoder ();
	}
}

