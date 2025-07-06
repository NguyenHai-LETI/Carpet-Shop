package com.example.carpetshop.repository;

import com.example.carpetshop.dto.CarpetDTO;
import com.example.carpetshop.entity.Carpet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarpetRepository extends JpaRepository<Carpet, Long>, CarpetRepositoryCustom {
    @Query("""
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
""")
    List<CarpetDTO> findAllWithMainAndHoverImage();

}

