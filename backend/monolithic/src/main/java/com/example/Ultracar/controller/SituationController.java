package com.example.Ultracar.controller;

import com.example.Ultracar.enums.Situation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/situation")
public class SituationController {
    @GetMapping
    public ResponseEntity<List<String>> getSituations() {
        List<String> situations
                = Arrays.stream(Situation.values()).map(Enum::name).toList();
        return ResponseEntity.ok(situations);
    }
}
