package com.zerolatency.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class users {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String username;
    private String password;

    private String role;
    private LocalDate created_at = LocalDate.now();
}
