package com.example.demo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GenerateInsertStatements {

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("insert_statements.sql"))) {
            for (int i = 1; i <= 1000; i++) {
                String username = "user" + i;
                String email = "user" + i + "@example.com";
                String password = "password" + i;
                String role = "ROLE_USER";
                String authorities = "USER";
                boolean enabled = true;

                String sql = String.format(
                        "INSERT INTO users (username, email, password, role, authorities, enabled) VALUES ('%s', '%s', '%s', '%s', '%s', %b);",
                        username, email, password, role, authorities, enabled
                );

                writer.println(sql);
            }

            System.out.println("SQL insert statements generated successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
