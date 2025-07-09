package com.example.carpetshop.repository;

import com.example.carpetshop.dto.CarpetDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.*;
import com.example.carpetshop.entity.Carpet;

@Repository
public class CarpetRepositoryImpl implements CarpetRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CarpetDTO> findFilteredCarpets(String keyword, List<String> sizes, List<String> colors, List<String> types, String sort) {
        if (keyword == null) keyword = "";
        if (sizes == null) sizes = List.of();
        if (colors == null) colors = List.of();
        if (types == null) types = List.of();

        // ======= Bước 1: Lấy các ID thỏa điều kiện =======
        StringBuilder idQuerySb = new StringBuilder("""
            SELECT DISTINCT c.id
            FROM Carpet c
            JOIN CarpetColorOption cco ON c.id = cco.carpet.id
            JOIN CarpetOption co ON cco.id = co.carpetColorOption.id
            JOIN Size s ON co.size.id = s.id
            JOIN Color color ON cco.color.id = color.id
            LEFT JOIN CarpetType ct ON c.id = ct.carpet.id
            LEFT JOIN Type t ON ct.type.id = t.id
            WHERE 1=1
        """);

        if (!keyword.isEmpty()) {
            idQuerySb.append(" AND LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))");
        }
        if (!sizes.isEmpty()) {
            idQuerySb.append(" AND s.value IN :sizes");
        }
        if (!colors.isEmpty()) {
            idQuerySb.append(" AND color.value IN :colors");
        }
        if (!types.isEmpty()) {
            idQuerySb.append(" AND t.value IN :types");
        }

        TypedQuery<Long> idQuery = entityManager.createQuery(idQuerySb.toString(), Long.class);
        if (!keyword.isEmpty()) idQuery.setParameter("keyword", keyword);
        if (!sizes.isEmpty()) idQuery.setParameter("sizes", sizes);
        if (!colors.isEmpty()) idQuery.setParameter("colors", colors);
        if (!types.isEmpty()) idQuery.setParameter("types", types);

        List<Long> carpetIds = idQuery.getResultList();
        if (carpetIds.isEmpty()) {
            return new ArrayList<>();
        }

        // ======= Bước 2: Lấy thông tin CarpetDTO + minPrice =======
        StringBuilder dtoQuerySb = new StringBuilder("""
            SELECT new com.example.carpetshop.dto.CarpetDTO(
                c.id,
                c.name,
                c.origin,
                c.shortDesc,
                (
                    SELECT img1.url FROM Img img1
                    WHERE img1.carpetColorOption.carpet.id = c.id
                    AND img1.isMain = true
                    ORDER BY img1.id ASC
                    LIMIT 1
                ),
                (
                    SELECT img2.url FROM Img img2
                    WHERE img2.carpetColorOption.carpet.id = c.id
                    AND img2.isHover = true
                    ORDER BY img2.id ASC
                    LIMIT 1
                ),
                (
                    SELECT MIN(co.price) FROM CarpetOption co
                    WHERE co.carpetColorOption.carpet.id = c.id
                )
            )
            FROM Carpet c
            WHERE c.id IN :ids
        """);

        // === Sắp xếp ===
        if (sort != null) {
            switch (sort) {
                case "name-asc" -> dtoQuerySb.append(" ORDER BY c.name ASC");
                case "name-desc" -> dtoQuerySb.append(" ORDER BY c.name DESC");
                case "price-asc" -> dtoQuerySb.append("""
                    ORDER BY (
                        SELECT MIN(co.price)
                        FROM CarpetOption co
                        WHERE co.carpetColorOption.carpet.id = c.id
                    ) ASC
                """);
                case "price-desc" -> dtoQuerySb.append("""
                    ORDER BY (
                        SELECT MIN(co.price)
                        FROM CarpetOption co
                        WHERE co.carpetColorOption.carpet.id = c.id
                    ) DESC
                """);
            }
        }

        TypedQuery<CarpetDTO> dtoQuery = entityManager.createQuery(dtoQuerySb.toString(), CarpetDTO.class);
        dtoQuery.setParameter("ids", carpetIds);

        // Trả kết quả duy nhất theo id
        Map<Long, CarpetDTO> uniqueMap = new LinkedHashMap<>();
        for (CarpetDTO dto : dtoQuery.getResultList()) {
            if (!uniqueMap.containsKey(dto.getId()) || (dto.getImageUrl() != null && dto.getHoverImageUrl() != null)) {
                uniqueMap.put(dto.getId(), dto);
            }
        }

        return new ArrayList<>(uniqueMap.values());
    }

    // Eagerly fetch all relations needed for homepage
    public List<Carpet> findAllWithAllRelations() {
        String jpql = """
            SELECT DISTINCT c FROM Carpet c
            LEFT JOIN FETCH c.colorOptions co
            LEFT JOIN FETCH co.color col
        """;
        return entityManager.createQuery(jpql, com.example.carpetshop.entity.Carpet.class).getResultList();
    }
}
