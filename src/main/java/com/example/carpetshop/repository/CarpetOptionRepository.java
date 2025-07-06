package com.example.carpetshop.repository;

import com.example.carpetshop.entity.CarpetOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarpetOptionRepository extends JpaRepository<CarpetOption, Long> {
    // Bạn có thể thêm các custom query sau này nếu cần
}
