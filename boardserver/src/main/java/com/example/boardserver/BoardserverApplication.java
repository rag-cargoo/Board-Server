package com.example.boardserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.boardserver.mapper")
public class BoardserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardserverApplication.class, args);
	}

}
