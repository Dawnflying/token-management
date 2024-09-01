package com.example.demo.controller;

import com.example.demo.service.DataLoaderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/biz")
public class BizController {

    /**
     * 业务方法
     *
     * @return 业务结果
     */
    @GetMapping("/bizMethod")
    public String bizMethod() {
        return "Biz method called!";
    }

}
