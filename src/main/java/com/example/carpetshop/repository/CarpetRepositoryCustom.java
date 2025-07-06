package com.example.carpetshop.repository;

import com.example.carpetshop.dto.CarpetDTO;
import java.util.List;

public interface CarpetRepositoryCustom {
    List<CarpetDTO> findFilteredCarpets(String keyword, List<String> sizes, List<String> colors, List<String> types, String sort);
}
