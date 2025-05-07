package com.example;

import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        String dbUser = dotenv.get("DB_USER");
        String dbPass = dotenv.get("DB_PASS");

        System.out.println("Usuario: " + dbUser);
        System.out.println("Password: " + dbPass);
    }
}