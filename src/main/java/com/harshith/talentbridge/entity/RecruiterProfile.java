package com.harshith.talentbridge.entity;

import com.harshith.talentbridge.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "recruiter_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Recruiter Personal Info ---
    @Column(name = "designation")
    private String designation;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "alternate_email")
    private String alternateEmail;

    // --- Company Corporate Info ---
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_website")
    private String companyWebsite;

    @Column(name = "company_location")
    private String companyLocation;

    @Column(name = "headquarters_address", length = 500)
    private String headquartersAddress;

    @Column(name = "industry")
    private String industry;

    @Column(name = "company_size")
    private String companySize;

    @Column(name = "established_year")
    private Integer establishedYear;

    @Column(name = "company_description", length = 2000)
    private String companyDescription;

    // --- Media & Socials ---
    @Column(name = "company_logo_url")
    private String companyLogoUrl;

    @Column(name = "company_cover_image_url")
    private String companyCoverImageUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "twitter_url")
    private String twitterUrl;

    // --- Verification & Admin Fields ---
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(name = "verification_notes")
    private String verificationNotes;

    // --- Timestamps ---
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- User Relationship ---
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false)
    private User user;
}