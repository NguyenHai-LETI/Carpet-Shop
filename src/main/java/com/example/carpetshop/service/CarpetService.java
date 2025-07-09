package com.example.carpetshop.service;

import com.example.carpetshop.dto.CarpetDTO;
import com.example.carpetshop.dto.CarpetDetailDTO;
import com.example.carpetshop.dto.CarpetSummaryDTO;
import com.example.carpetshop.dto.VariantOptionDTO;
import com.example.carpetshop.entity.Carpet;
import com.example.carpetshop.entity.Img;
import com.example.carpetshop.entity.CarpetColorOption;
import com.example.carpetshop.entity.CarpetOption;
import com.example.carpetshop.repository.CarpetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarpetService {
    @Autowired
    private CarpetRepository carpetRepository;

    @Transactional
    public CarpetDetailDTO getCarpetDetailById(Long id) {
        Carpet carpet = carpetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thảm"));

        CarpetDetailDTO dto = new CarpetDetailDTO();
        dto.setId(carpet.getId());
        dto.setName(carpet.getName());
        dto.setOrigin(carpet.getOrigin());
        dto.setShortDescription(carpet.getShortDesc());

        // Lấy ảnh từ CarpetColorOption -> Img
        List<String> imageUrls = carpet.getColorOptions().stream()
                .flatMap(opt -> opt.getImages().stream())
                .map(img -> img.getUrl())
                .distinct()
                .collect(Collectors.toList());
        dto.setImageUrls(imageUrls);

        // Lấy Color
        List<String> colors = carpet.getColorOptions().stream()
                .map(opt -> opt.getColor().getValue())
                .distinct()
                .collect(Collectors.toList());
        dto.setColors(colors);

        // Lấy Size, Type, và VariantOptions
        Set<String> sizes = new HashSet<>();
        Set<String> types = new HashSet<>();
        List<VariantOptionDTO> variantOptions = new ArrayList<>();

        for (CarpetColorOption colorOption : carpet.getColorOptions()) {
            String color = colorOption.getColor().getValue();

            for (CarpetOption option : colorOption.getOptions()) {
                String size = option.getSize().getValue();

                sizes.add(size);

                VariantOptionDTO variantDTO = new VariantOptionDTO(
                        option.getId(),
                        color,
                        size,
                        option.getPrice(),
                        option.getDiscount(),
                        option.getStock()
                );
                variantOptions.add(variantDTO);
            }
        }

        dto.setSizes(new ArrayList<>(sizes));
        dto.setTypes(new ArrayList<>(types));
        dto.setVariantOptions(variantOptions);

        return dto;
    }

    public List<CarpetDTO> filterCarpets(String keyword, List<String> sizes, List<String> colors, List<String> types, String sort) {
        sizes = sizes == null ? Collections.emptyList() : sizes;
        colors = colors == null ? Collections.emptyList() : colors;
        types = types == null ? Collections.emptyList() : types;
        return carpetRepository.findFilteredCarpets(keyword, sizes, colors, types, sort);
    }

    public List<CarpetDTO> getAllCarpetDTOsForHomepage() {
        List<CarpetDTO> baseList = carpetRepository.findAllWithMainAndHoverImage();
        // Fetch đầy đủ Carpet với các quan hệ (tránh N+1 bằng join fetch nếu cần tối ưu hơn)
        List<Carpet> carpets = carpetRepository.findAllWithAllRelations();
        Map<Long, Carpet> carpetMap = new HashMap<>();
        for (Carpet c : carpets) {
            // ép fetch các quan hệ
            c.getColorOptions().size();
            c.getCarpetTypes().size();
            carpetMap.put(c.getId(), c);
        }
        for (CarpetDTO dto : baseList) {
            Carpet carpet = carpetMap.get(dto.getId());
            if (carpet != null) {
                // Lấy list màu sắc
                List<String> colors = carpet.getColorOptions() != null ?
                    carpet.getColorOptions().stream()
                        .map(opt -> opt.getColor() != null ? opt.getColor().getValue() : null)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList()) : new ArrayList<>();
                dto.setColors(colors);
                // Lấy list loại
                List<String> types = carpet.getCarpetTypes() != null ?
                    carpet.getCarpetTypes().stream()
                        .map(ct -> ct.getType() != null ? ct.getType().getValue() : null)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList()) : new ArrayList<>();
                dto.setTypes(types);
            }
        }
        return baseList;
    }

    public List<CarpetSummaryDTO> getCarpetSummaries() {
        List<Carpet> carpets = carpetRepository.findAll();

        List<CarpetSummaryDTO> result = new ArrayList<>();

        for (Carpet carpet : carpets) {
            int totalStock = carpet.getColorOptions().stream()
                    .flatMap(colorOpt -> colorOpt.getOptions().stream())
                    .mapToInt(opt -> opt.getStock() != null ? opt.getStock() : 0)
                    .sum();

            result.add(new CarpetSummaryDTO(
                    carpet.getId(),
                    carpet.getName(),
                    totalStock
            ));
        }

        return result;
    }

}
