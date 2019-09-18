package com.pancarte.architecte.repository;

import com.pancarte.architecte.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("materialRepository")
public interface MaterialRepository  extends JpaRepository<Material, Long> {
    Material findByName(String material);

}
