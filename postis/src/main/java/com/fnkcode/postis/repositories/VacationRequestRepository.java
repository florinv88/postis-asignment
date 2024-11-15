package com.fnkcode.postis.repositories;

import com.fnkcode.postis.entities.VacationRequest;
import com.fnkcode.postis.records.OverlappingRequests;
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

    @Query("""
                SELECT new com.fnkcode.postis.records.OverlappingRequests(
                    new com.fnkcode.postis.dto.ManagerRequestDTO(
                        v1.id,
                        v1.author,
                        CASE
                            WHEN v1.status = '0' THEN 'PENDING'
                            WHEN v1.status = '1' THEN 'ACCEPTED'
                            ELSE 'REJECTED'
                        END,
                        v1.resolvedBy,
                        v1.requestCreatedAt,
                        v1.vacationStartDate,
                        v1.vacationEndDate
                    ),
                    new com.fnkcode.postis.dto.ManagerRequestDTO(
                        v2.id, 
                        v2.author, 
                        CASE 
                            WHEN v2.status = '0' THEN 'PENDING'
                            WHEN v2.status = '1' THEN 'ACCEPTED'
                            ELSE 'REJECTED'
                        END, 
                        v2.resolvedBy, 
                        v2.requestCreatedAt, 
                        v2.vacationStartDate, 
                        v2.vacationEndDate
                    ) 
                )
                FROM VacationRequest v1
                JOIN VacationRequest v2
                ON v1.id < v2.id
                   AND v1.vacationStartDate <= v2.vacationEndDate
                   AND v1.vacationEndDate >= v2.vacationStartDate
                ORDER BY v1.vacationStartDate
            """)
    List<OverlappingRequests> findOverlappingRequests();


}
