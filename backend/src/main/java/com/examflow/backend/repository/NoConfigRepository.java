package com.examflow.backend.repository;

import com.examflow.backend.entity.NoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoConfigRepository extends JpaRepository<NoConfiguration, Integer> {

    NoConfiguration findByNoConfigName(String noConfigName);
}
