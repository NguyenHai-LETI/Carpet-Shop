package com.example.carpetshop.controller;

import com.example.carpetshop.dto.CarpetSummaryDTO;
import com.example.carpetshop.service.CarpetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/carpets")
public class AdminCarpetController {

    @Autowired
    private CarpetService carpetService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CarpetSummaryDTO>> getAllCarpetSummaries() {
        List<CarpetSummaryDTO> list = carpetService.getCarpetSummaries();
        return ResponseEntity.ok(list);
    }
}
