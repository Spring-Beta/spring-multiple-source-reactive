package com.example.multiplesourcereactive.model.target;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "combined_users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TargetUser {
    @Id
    private Long id;

    private String name;
    private String email;
    private String fullName;
    private String contact;
}
