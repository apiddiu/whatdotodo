package com.aldo.whatdotodo.controller.controller;

import com.aldo.whatdotodo.controller.model.Status;
import com.aldo.whatdotodo.controller.service.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statuses")
public class StatusController {
    @Autowired StatusRepository statusRepo;

    @RequestMapping(method = RequestMethod.GET)
    public List<Status> statuses() {
        return statusRepo.findAll();
    }
}
