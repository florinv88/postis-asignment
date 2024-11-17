create table if not exists vacation_request
(
    id                  BIGINT     not null auto_increment,
    author              BIGINT     not null,
    status_req          varchar(2) not null,
    resolved_by         BIGINT,
    request_created_at  DATETIME       not null,
    vacation_start_date DATE       not null,
    vacation_end_date   DATE       not null,
    request_updated_at  DATETIME,

    CONSTRAINT vacation_pk PRIMARY KEY (id)
);