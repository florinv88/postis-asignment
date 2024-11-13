package com.fnkcode.postis.repositories;

import com.fnkcode.postis.entities.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacationRequestRepository extends JpaRepository<VacationRequest,Long> {

    List<VacationRequest> findAllByAuthorAndStatus(long id, String status);
}
