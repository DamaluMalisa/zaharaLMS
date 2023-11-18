package com.zahara.lms.assessment.repository;

import com.zahara.lms.assessment.model.StudentSubmission;
import com.zahara.lms.shared.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentSubmissionRepository extends BaseRepository<StudentSubmission, Long> {
    @Override
    @Query(
            "select x from #{#entityName} x where x.deleted = false and"
                    + "(cast(x.id as string) like :search "
                    + " or cast(x.submissionDate as string) like :search or x.submissionMaterial like :search)")
    Page<StudentSubmission> findContaining(Pageable pageable, String search);

    @Query(
            "select x from #{#entityName} x where x.deleted = false and x.studentId = :studentId and"
                    + "(cast(x.id as string) like :search "
                    + " or cast(x.submissionDate as string) like :search or x.submissionMaterial like :search)")
    Page<StudentSubmission> findByStudentIdContaining(Long studentId, Pageable pageable, String search);

    @Query(
            "select x from #{#entityName} x where x.deleted = false and x.assessment.id = :assessmentId and"
                    + "(cast(x.id as string) like :search "
                    + " or cast(x.submissionDate as string) like :search or x.submissionMaterial like :search)")
    Page<StudentSubmission> findByAssessmentIdContaining(Long assessmentId, Pageable pageable, String search);

    @Query(
            "select x from #{#entityName} x where x.deleted = false and x.assessment.assessmentType.subjectId = :subjectId and"
                    + "(cast(x.id as string) like :search "
                    + " or cast(x.submissionDate as string) like :search or x.submissionMaterial like :search)")
    Page<StudentSubmission> findByAssessmentAssessmentTypeSubjectIdContaining(Long subjectId, Pageable pageable, String search);

    @Query("select case when count(x) > 0 then true else false end from #{#entityName} x where x.id = :id and x.deleted = false")
    boolean existsByIdNotDeleted(@Param("id") Long id);


    List<StudentSubmission> findByStudentIdAndDeletedFalse(Long studentId);

    List<StudentSubmission> findByAssessmentIdAndDeletedFalse(Long assessmentId);

    List<StudentSubmission> findByAssessmentAssessmentTypeSubjectIdAndDeletedFalse(Long subjectId);


}
