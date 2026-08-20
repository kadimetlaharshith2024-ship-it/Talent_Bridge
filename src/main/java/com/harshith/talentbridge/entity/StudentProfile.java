package com.harshith.talentbridge.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 15)
    private String phone;

    @Column(nullable = false, length = 150)
    private String university;

    @Column(nullable = false, length = 100)
    private String branch;

    @Column(length = 100)
    private String domain;

    @Column(length = 100)
    private String location;

    @Column(length = 500)
    private String skills;

    @Column(nullable = false)
    private Double cgpa;

    private Integer graduationYear;

    @Column(length = 1000)
    private String bio;

    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String resumeUrl;
}