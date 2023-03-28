package com.example.springbootdemothree.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@ComponentScan("com.example.springbootdemothree")
public class SpringbootDemoThreeApplication {

    private static final Logger LOG = LoggerFactory.getLogger(SpringbootDemoThreeApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringbootDemoThreeApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("地址: \thttp://127.0.0.1:{}", env.getProperty("server.port"));
    }

}
