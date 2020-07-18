package com.joyce.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringbootJpaApplication {

	static Logger logger = LoggerFactory.getLogger(SpringbootJpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaApplication.class, args);
		logger.info("系统启动完毕");
	}

}
