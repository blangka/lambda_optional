package com.hkmc.aws.model;

import lombok.Data;

@Data
public class User {
    private Address address;

    @Data
    public static class Address{
        private String postCode;
    }
}
