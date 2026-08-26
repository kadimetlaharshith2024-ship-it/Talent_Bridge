package com.harshith.talentbridge.service;

import com.harshith.talentbridge.dto.RecruiterProfileRequest;
import com.harshith.talentbridge.dto.RecruiterProfileResponse;
import com.harshith.talentbridge.entity.RecruiterProfile;
import com.harshith.talentbridge.entity.User;
import com.harshith.talentbridge.enums.VerificationStatus;
import com.harshith.talentbridge.repository.RecruiterRepository;
import com.harshith.talentbridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final UserRepository userRepository;

    // 1. Create or Overwrite Full Profile
    @Transactional
    public RecruiterProfileResponse saveOrUpdateProfile(String email, RecruiterProfileRequest request) {
        User user = getUserByEmail(email);

        RecruiterProfile profile = recruiterRepository.findByUser(user)
                .orElseGet(() -> RecruiterProfile.builder()
                        .user(user)
                        .verificationStatus(VerificationStatus.PENDING)
                        .build());

        profile.setDesignation(trimIfNotNull(request.getDesignation()));
        profile.setContactPhone(trimIfNotNull(request.getContactPhone()));
        profile.setAlternateEmail(trimIfNotNull(request.getAlternateEmail()));
        profile.setCompanyName(trimIfNotNull(request.getCompanyName()));
        profile.setCompanyWebsite(trimIfNotNull(request.getCompanyWebsite()));
        profile.setCompanyLocation(trimIfNotNull(request.getCompanyLocation()));
        profile.setHeadquartersAddress(trimIfNotNull(request.getHeadquartersAddress()));
        profile.setIndustry(trimIfNotNull(request.getIndustry()));
        profile.setCompanySize(trimIfNotNull(request.getCompanySize()));
        profile.setEstablishedYear(request.getEstablishedYear());
        profile.setCompanyDescription(trimIfNotNull(request.getCompanyDescription()));
        profile.setCompanyLogoUrl(trimIfNotNull(request.getCompanyLogoUrl()));
        profile.setCompanyCoverImageUrl(trimIfNotNull(request.getCompanyCoverImageUrl()));
        profile.setLinkedinUrl(trimIfNotNull(request.getLinkedinUrl()));
        profile.setTwitterUrl(trimIfNotNull(request.getTwitterUrl()));

        RecruiterProfile saved = recruiterRepository.save(profile);
        return mapToResponse(saved);
    }

    // 2. View Logged-in Recruiter Profile
    @Transactional(readOnly = true)
    public RecruiterProfileResponse getProfile(String email) {
        User user = getUserByEmail(email);
        RecruiterProfile profile = recruiterRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found for user: " + email));

        return mapToResponse(profile);
    }

    // 3. Dynamic Partial Update (Any combination of fields)
    @Transactional
    public RecruiterProfileResponse patchProfile(String email, Map<String, Object> updates) {
        User user = getUserByEmail(email);
        RecruiterProfile profile = recruiterRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found for user: " + email));

        updates.forEach((key, value) -> {
            if (value != null) {
                String strVal = value.toString().trim();
                switch (key) {
                    case "designation" -> profile.setDesignation(strVal);
                    case "contactPhone" -> profile.setContactPhone(strVal);
                    case "alternateEmail" -> profile.setAlternateEmail(strVal);
                    case "companyName" -> profile.setCompanyName(strVal);
                    case "companyWebsite" -> profile.setCompanyWebsite(strVal);
                    case "companyLocation" -> profile.setCompanyLocation(strVal);
                    case "headquartersAddress" -> profile.setHeadquartersAddress(strVal);
                    case "industry" -> profile.setIndustry(strVal);
                    case "companySize" -> profile.setCompanySize(strVal);
                    case "companyDescription" -> profile.setCompanyDescription(strVal);
                    case "companyLogoUrl" -> profile.setCompanyLogoUrl(strVal);
                    case "companyCoverImageUrl" -> profile.setCompanyCoverImageUrl(strVal);
                    case "linkedinUrl" -> profile.setLinkedinUrl(strVal);
                    case "twitterUrl" -> profile.setTwitterUrl(strVal);
                    case "establishedYear" -> {
                        try {
                            profile.setEstablishedYear(Integer.valueOf(strVal));
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
        });

        RecruiterProfile updated = recruiterRepository.save(profile);
        return mapToResponse(updated);
    }

    // 4. Update Company Details Only
    @Transactional
    public RecruiterProfileResponse updateCompanyDetails(String email, String name, String website, String location, String industry, String size, Integer year) {
        User user = getUserByEmail(email);
        RecruiterProfile profile = getProfileEntity(user);

        if (isValid(name)) profile.setCompanyName(name.trim());
        if (isValid(website)) profile.setCompanyWebsite(website.trim());
        if (isValid(location)) profile.setCompanyLocation(location.trim());
        if (isValid(industry)) profile.setIndustry(industry.trim());
        if (isValid(size)) profile.setCompanySize(size.trim());
        if (year != null && year > 1800) profile.setEstablishedYear(year);

        return mapToResponse(recruiterRepository.save(profile));
    }

    // 5. Update Contact Details Only
    @Transactional
    public RecruiterProfileResponse updateContactInfo(String email, String phone, String alternateEmail, String address) {
        User user = getUserByEmail(email);
        RecruiterProfile profile = getProfileEntity(user);

        if (isValid(phone)) profile.setContactPhone(phone.trim());
        if (isValid(alternateEmail)) profile.setAlternateEmail(alternateEmail.trim());
        if (isValid(address)) profile.setHeadquartersAddress(address.trim());

        return mapToResponse(recruiterRepository.save(profile));
    }

    // 6. Update Media & Socials (Logos, Banners, Social Handles)
    @Transactional
    public RecruiterProfileResponse updateMediaAndSocials(String email, String logoUrl, String coverUrl, String linkedin, String twitter) {
        User user = getUserByEmail(email);
        RecruiterProfile profile = getProfileEntity(user);

        if (isValid(logoUrl)) profile.setCompanyLogoUrl(logoUrl.trim());
        if (isValid(coverUrl)) profile.setCompanyCoverImageUrl(coverUrl.trim());
        if (isValid(linkedin)) profile.setLinkedinUrl(linkedin.trim());
        if (isValid(twitter)) profile.setTwitterUrl(twitter.trim());

        return mapToResponse(recruiterRepository.save(profile));
    }

    // 7. Update Company Description / Bio Only
    @Transactional
    public RecruiterProfileResponse updateDescription(String email, String description) {
        User user = getUserByEmail(email);
        RecruiterProfile profile = getProfileEntity(user);

        if (isValid(description)) profile.setCompanyDescription(description.trim());

        return mapToResponse(recruiterRepository.save(profile));
    }

    // --- Helpers ---
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    private RecruiterProfile getProfileEntity(User user) {
        return recruiterRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found for user: " + user.getEmail()));
    }

    private boolean isValid(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private String trimIfNotNull(String str) {
        return (str != null) ? str.trim() : null;
    }

    public RecruiterProfileResponse mapToResponse(RecruiterProfile profile) {
        return RecruiterProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .userEmail(profile.getUser().getEmail())
                .designation(profile.getDesignation())
                .contactPhone(profile.getContactPhone())
                .alternateEmail(profile.getAlternateEmail())
                .companyName(profile.getCompanyName())
                .companyWebsite(profile.getCompanyWebsite())
                .companyLocation(profile.getCompanyLocation())
                .headquartersAddress(profile.getHeadquartersAddress())
                .industry(profile.getIndustry())
                .companySize(profile.getCompanySize())
                .establishedYear(profile.getEstablishedYear())
                .companyDescription(profile.getCompanyDescription())
                .companyLogoUrl(profile.getCompanyLogoUrl())
                .companyCoverImageUrl(profile.getCompanyCoverImageUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .twitterUrl(profile.getTwitterUrl())
                .verificationStatus(profile.getVerificationStatus())
                .verificationNotes(profile.getVerificationNotes())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}