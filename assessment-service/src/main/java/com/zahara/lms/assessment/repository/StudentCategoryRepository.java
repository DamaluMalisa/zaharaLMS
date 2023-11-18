package com.zahara.lms.assessment.repository;

import com.zahara.lms.assessment.model.StudentCategory;
import com.zahara.lms.shared.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCategoryRepository extends BaseRepository<StudentCategory, Long> {
    @Override
    @Query(
            "select x from #{#entityName} x where x.deleted = false "
                    + "and (cast(x.id as string) like :search or x.name like :search)")
    Page<StudentCategory> findContaining(Pageable pageable, String search);
}
