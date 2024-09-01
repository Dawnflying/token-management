package com.example.demo.domain;

import lombok.Data;

@Data
public class DemoResponse<T> {
    private String code;
    private String message;
    private T data;

    public DemoResponse<T> success(T data) {
        this.code = "200";
        this.message = "success";
        this.data = data;
        return this;
    }

    public DemoResponse<T> fail(String code, String message) {
        DemoResponse<T> response = new DemoResponse<>();
        this.code = code;
        this.message = message;
        return this;
    }
}
