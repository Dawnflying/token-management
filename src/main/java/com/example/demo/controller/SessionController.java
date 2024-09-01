package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/session")
public class SessionController {

    /**
     * 设置 Session
     *
     * @param value   值
     * @param session 会话
     * @return 设置结果
     */
    @GetMapping("/setSession")
    public String setSession(@RequestParam String value, HttpSession session) {
        session.setAttribute("myAttribute", value);
        return "Session value set!";
    }

    /**
     * 获取 Session
     *
     * @param session 会话
     * @return Session 值
     */
    @GetMapping("/getSession")
    public String getSession(HttpSession session) {
        return JSON.toJSONString(session.getAttributeNames());
    }
}
