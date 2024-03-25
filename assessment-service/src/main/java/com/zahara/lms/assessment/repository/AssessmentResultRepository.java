package com.zahara.lms.assessment.repository;

import com.zahara.lms.assessment.model.AssessmentResult;
import com.zahara.lms.shared.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentResultRepository extends BaseRepository<AssessmentResult, Long> {

    @Override
    @Query(
            "select x from #{#entityName} x where x.deleted =  false and "
                    + "(cast(x.id as string) like :search "
                    + "or cast(x.scores as string) like :search "
                    + "or x.feedback like :search)")
    Page<AssessmentResult> findContaining(Pageable pageable, String search);

    @Query(
            "select x from #{#entityName} x where x.deleted = false and x.studentSubmission.id = :studentSubmissionId and "
                    + "(cast(x.id as string) like :search "
                    + "or cast(x.scores as string) like :search "
                    + "or x.feedback like :search)")
    Page<AssessmentResult> findByStudentSubmissionIdContaining(Long studentSubmissionId, Pageable pageable, String search);

    @Query(
            "select x from #{#entityName} x where x.deleted = false and x.subjectEnrollmentId in :subjectEnrollmentIds and "
                    + "(cast(x.id as string) like :search "
                    + "or cast(x.scores as string) like :search "
                    + "or x.feedback like :search)")
    Page<AssessmentResult> findBySubjectEnrollmentIdsContaining(
            List<Long> subjectEnrollmentIds, Pageable pageable, String search);

    List<AssessmentResult> findByStudentSubmissionIdAndDeletedFalse(Long studentSubmissionId);

    List<AssessmentResult> findBySubjectEnrollmentIdInAndDeletedFalse(
            List<Long> subjectEnrollmentIds);

}
