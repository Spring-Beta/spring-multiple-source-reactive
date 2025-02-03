package com.example.multiplesourcereactive.controller;

import com.example.multiplesourcereactive.constant.ApplicationConstant;
import com.example.multiplesourcereactive.model.target.TargetUser;
import com.example.multiplesourcereactive.service.DataMigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(ApplicationConstant.BASE_PATH)
public class MigrationController {

    private final DataMigrationService migrationService;

    @Autowired
    public MigrationController(DataMigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @GetMapping
    public ResponseEntity<Flux<TargetUser>> migrateData() {

        return ResponseEntity.ok(migrationService.migrateData());
    }
}

