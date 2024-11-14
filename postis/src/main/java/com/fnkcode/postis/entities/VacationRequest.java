package com.fnkcode.postis.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "vacation_request")
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
    private String resolvedBy;

    @Column(name = "request_created_at", updatable = false)
    private Date requestCreatedAt;

    @Column(name = "vacation_start_date", updatable = false)
    private Date vacationStartDate;

    @Column(name = "vacation_end_date", updatable = false)
    private Date vacationEndDate;

    @Column(name = "request_updated_at", insertable = false)
    private Date requestUpdatedAt;
}
