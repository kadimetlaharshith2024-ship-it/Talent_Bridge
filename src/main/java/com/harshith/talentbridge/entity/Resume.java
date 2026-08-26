package com.harshith.talentbridge.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resumes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "version_number"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String storagePath;

    @Column(nullable = false, name = "version_number")
    private Integer versionNumber; // 1, 2, or 3

    @Column(length = 255)
    private String title; // e.g., "Backend Specialist Resume", "Data Science Resume"

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDefault = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime uploadedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}