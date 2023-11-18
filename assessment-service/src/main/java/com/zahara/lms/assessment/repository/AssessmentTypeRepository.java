package com.zahara.lms.assessment.repository;

import com.zahara.lms.assessment.model.AssessmentType;
import com.zahara.lms.shared.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentTypeRepository extends BaseRepository<AssessmentType, Long> {
    @Override
    @Query(
            "select x from #{#entityName} x where x.deleted = false and"
                    + "(cast(x.id as string) like :search or x.name like :search or cast(x.maxMarks as string) like :search)")
    Page<AssessmentType> findContaining(Pageable pageable, String search);


    @Query(
            "select x from #{#entityName} x where x.deleted = false and x.subjectId = :subjectId "
                    + "and (cast(x.id as string) like :search or x.name like :search or cast(x.maxMarks as string) like :search)")
    Page<AssessmentType> findBySubjectIdContaining(Long subjectId, Pageable pageable, String search);

    List<AssessmentType> findBySubjectIdAndDeletedFalse(Long subjectId);


}
