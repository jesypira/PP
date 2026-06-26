package com.pira.piraproject.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pira.piraproject.util.DATA;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    @Transient
    private Level nextLevel;

    @Builder.Default
    private Integer gold = 0;

    @Builder.Default
    private Integer xp = 0;

    @Builder.Default
    private Integer dailyStreak = 0;

    private LocalDate lastStreakUpdate;

    public User(Long id){
        this.id = id;
    }
}