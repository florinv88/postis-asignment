create table if not exists vacation_request
(
    id                  BIGINT     not null auto_increment,
    author              BIGINT     not null,
    status_req          varchar(1) not null,
    resolved_by         BIGINT,
    request_created_at  DATE       not null,
    vacation_start_date DATE       not null,
    vacation_end_date   DATE       not null,
    request_updated_at  DATE,

    CONSTRAINT vacation_pk PRIMARY KEY (id)
);