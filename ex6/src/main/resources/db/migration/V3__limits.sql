create sequence if not exists limit_seq start with 1 increment by 50;

create table if not exists limits
(
    id        bigint primary key,
    client_id bigint         not null,
    day       date           not null,
    remaining numeric(19, 2) not null,
    version   bigint         not null default 0
);

create unique index if not exists ux_limits_client_day on limits (client_id, day);

create index if not exists ix_limits_client on limits (client_id);

alter table payments add column if not exists external_id varchar(64);

create unique index if not exists ux_pay_external
    on payments(external_id)
    where external_id is not null;

