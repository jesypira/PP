package com.pira.piraproject.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Builder.Default
    private Integer level = 1;

    @Builder.Default
    private Integer gold = 0;

    @Builder.Default
    private Integer dailyStreak = 0;

    public User(Long id){
        this.id = id;
    }
}