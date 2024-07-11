package com.sparta.catubebatch.repository;

import com.sparta.catubebatch.entity.AdBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdBillRepository extends JpaRepository<AdBill, Long> {
}
