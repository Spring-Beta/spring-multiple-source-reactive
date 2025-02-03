package com.example.multiplesourcereactive.controller.secondary;

import com.example.multiplesourcereactive.constant.ApplicationConstant;
import com.example.multiplesourcereactive.model.secondary.SecondaryCustomer;
import com.example.multiplesourcereactive.service.secondary.ISecondaryCustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(ApplicationConstant.SECONDARY_BASE_PATH)
public class SecondaryCustomerController {
    private final ISecondaryCustomerService secondaryCustomerService;

    public SecondaryCustomerController(ISecondaryCustomerService secondaryCustomerService) {
        this.secondaryCustomerService = secondaryCustomerService;
    }

    @PostMapping(ApplicationConstant.SAVE)
    public ResponseEntity<Mono<SecondaryCustomer>> save(@RequestBody SecondaryCustomer secondaryCustomer) {
        return ResponseEntity.ok(secondaryCustomerService.save(secondaryCustomer));
    }
}
