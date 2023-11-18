package com.zahara.lms.assessment.repository;

import com.zahara.lms.assessment.model.Assessment;
import com.zahara.lms.shared.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends BaseRepository<Assessment, Long> {
    @Override
    @Query(
            "select x from #{#entityName} x where x.deleted = false "
                    + "and (cast(x.id as string) like :search "
                    + "or x.title like :search or x.description like :search "
                    + "or cast(x.createdDate as string) like :search or cast(x.dueDate as string) like :search "
                    + "or x.sourceUrl like :search)")
    Page<Assessment> findContaining(Pageable pageable, String search);

    @Query(
            "select x from #{#entityName} x where x.deleted = false and x.assessmentType.subjectId = :subjectId "
                    + "and (cast(x.id as string) like :search "
                    + "or x.title like :search or x.description like :search "
                    + "or cast(x.createdDate as string) like :search or cast(x.dueDate as string) like :search "
                    + "or x.sourceUrl like :search)")
    Page<Assessment> findByAssessmentTypeSubjectIdContaining(Long subjectId, Pageable pageable, String search);


    @Query(
            "select x from #{#entityName} x where x.deleted = false and x.assessmentType.id = :assessmentTypeId "
                    + "and (cast(x.id as string) like :search "
                    + "or x.title like :search or x.description like :search "
                    + "or cast(x.createdDate as string) like :search or cast(x.dueDate as string) like :search "
                    + "or x.sourceUrl like :search)")
    Page<Assessment> findByAssessmentTypeIdContaining(Long assessmentTypeId, Pageable pageable, String search);

    @Query(
            "select x from #{#entityName} x where x.deleted = false and x.studentCategory.id = :studentCategoryId "
                    + "and (cast(x.id as string) like :search "
                    + "or x.title like :search or x.description like :search "
                    + "or cast(x.createdDate as string) like :search or cast(x.dueDate as string) like :search "
                    + "or x.sourceUrl like :search)")
    Page<Assessment> findByStudentCategoryIdContaining(Long studentCategoryId, Pageable pageable, String search);

    @Query("select case when count(x) > 0 then true else false end from #{#entityName} x where x.id = :id and x.deleted = false")
    boolean existsByIdNotDeleted(@Param("id") Long id);

    List<Assessment> findByAssessmentTypeIdAndDeletedFalse(Long assessmentTypeId);

    List<Assessment> findByStudentCategoryIdAndDeletedFalse(Long studentCategoryId);

    List<Assessment> findByAssessmentTypeSubjectIdAndDeletedFalse(Long subjectId);



}
