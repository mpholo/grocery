package com.mpholo.project.grocery.v1;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractRestController {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw  new RuntimeException();
        }
    }
}
