package com.example.carpetshop.controller;

import com.example.carpetshop.dto.CarpetDTO;
import com.example.carpetshop.dto.CarpetDetailDTO;
import com.example.carpetshop.repository.CarpetRepository;
import com.example.carpetshop.service.CarpetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carpets")
@CrossOrigin(origins = "http://localhost:3000")
public class CarpetController {

    @Autowired
    private CarpetRepository carpetRepository;

    @GetMapping("/with-image")
    public List<CarpetDTO> getAllCarpetsWithImage() {
            return carpetRepository.findAllWithMainAndHoverImage();
    }



    @Autowired
    private CarpetService carpetService;
    @GetMapping("/{id}")
    public ResponseEntity<CarpetDetailDTO> getCarpetDetail(@PathVariable Long id) {
        return ResponseEntity.ok(carpetService.getCarpetDetailById(id));
    }


    private static final Logger logger = LoggerFactory.getLogger(CarpetController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/filter")
    public ResponseEntity<List<CarpetDTO>> filterCarpets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> sizes,
            @RequestParam(required = false) List<String> colors,
            @RequestParam(required = false) List<String> types,
            @RequestParam(required = false) String sort
    ) {
        List<CarpetDTO> filtered = carpetService.filterCarpets(keyword, sizes, colors, types, sort);

        try {
            String jsonOutput = objectMapper.writeValueAsString(filtered);
            logger.info("✅ JSON trả về từ /filter: {}", jsonOutput);
        } catch (JsonProcessingException e) {
            logger.error("❌ Không thể log JSON kết quả /filter", e);
        }
        return ResponseEntity.ok(filtered);
    }




}