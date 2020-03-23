package com.atharva.auth.core;

import java.util.Base64;

public class TrialMain {
    public static void main(String[] args) {
        System.out.println("admin: " + Base64.getEncoder().encodeToString("test-admin".getBytes()));
        System.out.println("project: " + Base64.getEncoder().encodeToString("test-project".getBytes()));
        System.out.println("pass: " + Base64.getEncoder().encodeToString("123456789".getBytes()));
    }
}
