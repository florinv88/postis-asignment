package com.fnkcode.postis.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "vacation_request")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor
public class VacationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(updatable = false)
    private long author;

    @Column(name = "status_req")
    private String status;

    @Column(name = "resolved_by")
    private Long resolvedBy;

    @Column(name = "request_created_at", updatable = false)
    @CreatedDate
    private LocalDate requestCreatedAt;

    @Column(name = "vacation_start_date", updatable = false)
    private LocalDate vacationStartDate;

    @Column(name = "vacation_end_date", updatable = false)
    private LocalDate vacationEndDate;

    @Column(name = "request_updated_at", insertable = false)
    @LastModifiedDate
    private LocalDate requestUpdatedAt;
}
