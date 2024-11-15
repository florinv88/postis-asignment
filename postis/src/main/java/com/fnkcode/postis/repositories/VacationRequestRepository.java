package com.fnkcode.postis.repositories;

import com.fnkcode.postis.entities.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    List<VacationRequest> findAllByAuthorAndStatus(long id, String status);


    @Query("""
            select sum(datediff(vacationEndDate,vacationStartDate))
            from VacationRequest
            where author =:id and status = '1'
            """)
    long getNumberOfVacationDaysTakenBy(@Param("id") long id);

    List<VacationRequest> findAllByStatus(String status);
    List<VacationRequest> findAllByAuthor(long author);
    Optional<VacationRequest> findById(long id);
}
