package com.trip.mymy.common.jwt;

import java.util.Base64;

public class Base64Check {
    public static void main(String[] args) {
        String secretKey = "KJGHMYMYRGHJKLYTRVMYMYJKLPOIUYTREWQASDFGMYMYQWERTYUIOPMNBVCX";

        String base64EncodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        System.out.println(base64EncodedKey);
    }
}
