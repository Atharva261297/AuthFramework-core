package com.atharva.auth.core;

import java.util.Base64;

public class TrialMain {
    public static void main(String[] args) {
        System.out.println(Base64.getEncoder().encodeToString("test-admin".getBytes()));
        System.out.println(Base64.getEncoder().encodeToString("password".getBytes()));
    }
}
