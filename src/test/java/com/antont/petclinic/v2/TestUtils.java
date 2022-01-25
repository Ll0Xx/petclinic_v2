package com.antont.petclinic.v2;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
