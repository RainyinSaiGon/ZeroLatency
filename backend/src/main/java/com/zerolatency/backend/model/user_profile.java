package com.zerolatency.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class user_profile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profile_id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    private users user;

    private String bio;
    private String avatar_url;
    private String first_name;
    private String last_name;

    private String current_location;
    private String hometown;
    private String occupation;
}
    