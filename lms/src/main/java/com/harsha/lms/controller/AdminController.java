package com.harsha.lms.controller;

import com.harsha.lms.model.KeyAccountManager;
import com.harsha.lms.service.KamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.PriorityQueue;

@RestController
@RequestMapping("api/admin")
public class AdminController {



    @Autowired
    private KamService kamService;

    @GetMapping("dashboard")
    public ResponseEntity<?> getLeaderBoard(@RequestParam String status){
        return new ResponseEntity<>(kamService.getLeaderboardByLeadStatus(status), HttpStatus.OK);

    }
}
