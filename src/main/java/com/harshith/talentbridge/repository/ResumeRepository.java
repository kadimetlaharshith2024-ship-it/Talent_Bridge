package com.harshith.talentbridge.repository;

import com.harshith.talentbridge.entity.Resume;
import com.harshith.talentbridge.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByStudentOrderByVersionNumberAsc(StudentProfile student);

    Optional<Resume> findByStudentAndVersionNumber(StudentProfile student, Integer versionNumber);

    Optional<Resume> findByStudentAndIsDefaultTrue(StudentProfile student);

    long countByStudent(StudentProfile student);
}