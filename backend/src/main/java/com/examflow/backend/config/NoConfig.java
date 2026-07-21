package com.examflow.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.examflow.backend.entity.NoConfiguration;
import com.examflow.backend.repository.NoConfigRepository;

@Component
public class NoConfig {

    private final NoConfigRepository noConfigRepository;

    @Autowired
    public NoConfig(NoConfigRepository noConfigRepository) {
        this.noConfigRepository = noConfigRepository;
    }

    public String instructorNoConfigCreation() {
        String instructorNo = "";
        NoConfiguration noConfig = noConfigRepository.findByNoConfigName("Instructor");
        if (noConfig != null) {
            Integer nextNo = noConfig.getNextNumber();
            String suffix = noConfig.getSuffix();
            instructorNo = suffix + nextNo;
            noConfig.setNextNumber(nextNo + 1);
            noConfigRepository.save(noConfig);

        }
        return instructorNo;
    }
}
