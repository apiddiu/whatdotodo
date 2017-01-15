package com.aldo.whatdotodo.controller;

import com.aldo.whatdotodo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/principal")
public class PrincipalController {
    @RequestMapping(method = RequestMethod.GET)
    public User principal(Principal principal) {
        return new User(principal.getName());
    }
}
