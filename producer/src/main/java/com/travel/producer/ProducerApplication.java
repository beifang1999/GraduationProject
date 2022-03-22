package com.travel.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ProducerApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(ProducerApplication.class, args);
		MockTask mockTask = context.getBean(MockTask.class);

		//传入 log 或 db 作为 flag 来控制模拟产生 哪一种数据
		mockTask.mainTask("log",1000,1000,4);

	}
}
