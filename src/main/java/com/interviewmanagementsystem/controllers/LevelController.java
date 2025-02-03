package com.interviewmanagementsystem.controllers;

import com.ninja_in_pyjamas.enums.Level;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors; 
@RestController
@RequestMapping("/api/levels")
public class LevelController {

    @GetMapping
    public ResponseEntity<List<String>> getLevels() {
        List<String> levels = Arrays.stream(Level.values())
                                    .map(Level::name)
                                    .collect(Collectors.toList());
        return ResponseEntity.ok(levels);
    }
}
