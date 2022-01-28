package com.antont.petclinic.v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.antont.petclinic.v2", "asset.pipeline.springboot"})
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PetclinicV2Application {

    public static void main(String[] args) {
        SpringApplication.run(PetclinicV2Application.class, args);
    }

}
