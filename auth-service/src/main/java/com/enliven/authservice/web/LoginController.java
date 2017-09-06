package com.enliven.authservice.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Login API - Web Service Interface
 *
 * @author lattewalk
 */
@RestController
@RequestMapping("/")
public class LoginController {

    @RequestMapping({"/user", "/me"})
    public Map<String, String> getCurrentLoginUser(Principal principal) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", principal.getName());
        return map;
    }

}
