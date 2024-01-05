create table if not exists keycloak.user_entity
(
    id                          varchar(36)           not null
        constraint constraint_fb
            primary key,
    email                       varchar(255),
    email_constraint            varchar(255),
    email_verified              boolean default false not null,
    enabled                     boolean default false not null,
    federation_link             varchar(255),
    first_name                  varchar(255),
    last_name                   varchar(255),
    realm_id                    varchar(255),
    username                    varchar(255),
    created_timestamp           bigint,
    service_account_client_link varchar(255),
    not_before                  integer default 0     not null,
    constraint uk_dykn684sl8up1crfei6eckhd7
        unique (realm_id, email_constraint),
    constraint uk_ru8tt6t700s9v50bu18ws5ha6
        unique (realm_id, username)
);

alter table user_entity
    owner to postgres;

create index IF NOT EXISTS idx_user_email
    on user_entity (email);

create index IF NOT EXISTS idx_user_service_account
    on user_entity (realm_id, service_account_client_link);

CREATE TABLE if not exists keycloak.tasks
(
    id           serial PRIMARY KEY,
    title        text,
    description  text,
    status       varchar(20),
    priority     varchar(10),
    author_id    varchar(36) REFERENCES user_entity (id),
    performer_id varchar(36) REFERENCES user_entity (id)
);
CREATE TABLE if not exists keycloak.comments
(
    id           serial PRIMARY KEY,
    comment      text,
    user_comment varchar(36),
    task_comment int
);
CREATE TABLE IF NOT EXISTS keycloak.user_tasks
(
    id      serial PRIMARY KEY,
    user_id varchar(36) references user_entity (id),
    task_id int references tasks (id)
);
alter table keycloak.comments
    add constraint user_comment_fk foreign key (user_comment) references user_entity (id);
alter table keycloak.comments
    add constraint task_comment_fk foreign key (task_comment) references keycloak.tasks (id);
