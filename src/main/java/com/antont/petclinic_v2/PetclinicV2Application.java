package com.antont.petclinic_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PetclinicV2Application {

    public static void main(String[] args) {
        SpringApplication.run(PetclinicV2Application.class, args);
    }

}
