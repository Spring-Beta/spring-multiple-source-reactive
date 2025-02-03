package com.example.multiplesourcereactive.controller.primary;

import com.example.multiplesourcereactive.constant.ApplicationConstant;
import com.example.multiplesourcereactive.model.primary.PrimaryUser;
import com.example.multiplesourcereactive.service.primary.IPrimaryUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(ApplicationConstant.PRIMARY_BASE_PATH)
public class PrimaryUserController {
    private final IPrimaryUserService primaryUserService;

    public PrimaryUserController(IPrimaryUserService primaryUserService) {
        this.primaryUserService = primaryUserService;
    }

    @PostMapping(ApplicationConstant.SAVE)
    public ResponseEntity<Mono<PrimaryUser>> save(@RequestBody PrimaryUser primaryUser){
        return ResponseEntity.ok(primaryUserService.save(primaryUser));
    }
}
