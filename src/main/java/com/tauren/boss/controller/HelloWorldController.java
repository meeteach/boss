package com.tauren.boss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author niuyandong
 * @since 2017��9��23������12:45:13
 */
@Controller
public class HelloWorldController {
    
    @RequestMapping("/index.html")
    public String toQuery() {
        return "index";
    }
    
}
