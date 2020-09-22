create extension pgcrypto;

-- defines the db schema

-- SCHEMA: public
-- DROP SCHEMA IF EXISTS public ;

-- CREATE SCHEMA public
--     AUTHORIZATION "pi-group-6";

-- COMMENT ON SCHEMA public
--     IS 'standard public schema';

-- GRANT ALL ON SCHEMA public TO PUBLIC;

-- GRANT ALL ON SCHEMA public TO "pi-group-6";

----------------------------------------
--- Tables definition
create type "workshop_state" as enum ('published', 'closed');

alter type "workshop_state" owner to "pi-group-6";

create table admin
(
    "email"    varchar not null
        constraint "admin_pkey"
            primary key
        constraint check_email
            check (("email")::text ~ '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'::text),
    "password" varchar not null,
    "token" varchar
);

alter table admin
    owner to "pi-group-6";

create table category
(
    "title" varchar not null
        constraint "category_pkey"
            primary key,
    "description"    text    not null
);

alter table category
    owner to "pi-group-6";

create table location
(
    "npa"  smallint not null
        constraint check_npa
            check ((("npa")::numeric >= (1000)::numeric) AND (("npa")::numeric <= (9999)::numeric)),
    "city" varchar  not null,
    constraint "location_pkey"
        primary key ("npa", "city")
);

alter table location
    owner to "pi-group-6";

create table client
(
    "email"     varchar  not null
        constraint "client_pkey"
            primary key
        constraint check_email
            check (("email")::text ~ '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'::text),
    "password"  varchar  not null,
    "lastname"  varchar  not null,
    "firstname" varchar  not null,
    "street"    varchar  not null,
    "phone"     varchar
        constraint check_phone
            check (("phone")::text ~* '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'::text),
    "npa"              smallint not null,
    "city"             varchar  not null,
    constraint "fk_client_location"
        foreign key ("city", "npa") references location ("city", "npa")
            on update cascade on delete restrict,
    "token" varchar
);

alter table client
    owner to "pi-group-6";

create index "fki_fk_client_location"
    on client ("npa", "city");

create table organizer
(
    "email"     varchar  not null
        constraint "organizer_pkey"
            primary key
        constraint check_email
            check (("email")::text ~ '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'::text),
    "street"    varchar  not null,
    "lastname"  varchar  not null,
    "firstname" varchar  not null,
    "phone"     varchar
        constraint check_phone
            check (("phone")::text ~* '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'::text),
    "city"                varchar  not null,
    "npa"                 smallint not null,
    constraint "fk_organizer_location"
        foreign key ("city", "npa") references location ("city", "npa")
            on update cascade on delete restrict
);

alter table organizer
    owner to "pi-group-6";

create index "fki_fk_organizer_location"
    on organizer ("npa", "city");

create table workshop
(
    "title"       varchar          not null,
    constraint "workshop_pkey"
        primary key ("title", "date"),
    "date"        date        not null,
    "datetime_inscription" timestamp        not null,
    "datetime_publication" timestamp        not null,
        constraint check_datetime
            check (("date" > "datetime_inscription") AND ("datetime_inscription" > "datetime_publication")),
    "street"               varchar          not null,
    "description_xhtml"    xml              not null,
    "ingredients_xml"      xml              not null,
    "max_participants"     smallint         not null,
    "min_participants"     smallint         not null,
        constraint check_participants
            check (("max_participants" > 0) AND ("min_participants" <= "max_participants")),
    "state"                "workshop_state" not null,
    "title_category"       varchar          not null
        constraint "fk_workshop_category"
            references category
            on update cascade on delete restrict,
    "email_organizer"      varchar          not null
        constraint "fk_workshop_organizer"
            references organizer
            on update cascade on delete cascade,
    "npa"                  smallint         not null,
    "city"                 varchar          not null,
        constraint "fk_workshop_location"
            foreign key ("city", "npa") references location ("city", "npa")
                on update cascade on delete restrict,
    "image"        bytea,
    "lock_time"    timestamp
);

alter table workshop
    owner to "pi-group-6";

create index "fki_FK_Workshop_Category"
    on workshop ("title_category");

create index "fki_fk_workshop_organizer"
    on workshop ("email_organizer");

create index "fki_fk_workshop_location"
    on workshop ("npa", "city");

create table workshop_draft
(
    "title"       varchar   not null,
    "date"        date not null,
    constraint "workshop_draft_pkey"
        primary key ("title", "date"),
    "datetime_inscription" timestamp,
        constraint check_datetime
            check ("date" > "datetime_inscription"),
    "street"               varchar,
    "description_xhtml"    xml,
    "ingredients_xml"      xml,
    "max_participants"     smallint,
    "min_participants"     smallint,
        constraint check_participants
            check (("max_participants" > 0) AND ("min_participants" <= "max_participants")),
    "title_category"             varchar
        constraint "fk_workshop_draft_category"
            references category
            on update cascade on delete restrict,
    "email_organizer"            varchar
        constraint "fk_workshop_draft_organizer"
            references organizer
            on update cascade on delete cascade,
    "npa"                        smallint,
    "city"                       varchar,
        constraint "fk_workshop_draft_location"
            foreign key ("city", "npa") references location ("city", "npa")
                on update cascade on delete restrict,
    "image"        bytea,
    "lock_time" timestamp
);

alter table workshop_draft
    owner to "pi-group-6";

create index "fki_fk_workshop_draft_category"
    on workshop_draft ("title_category");

create index "fki_fk_workshop_draft_organizer"
    on workshop_draft ("email_organizer");

create index "fki_fk_workshop_draft_location"
    on workshop_draft ("npa", "city");

create table tr_client_workshop
(
    "email_client"   varchar   not null
        constraint "fk_tr_client_workshop_client"
            references client
            on update cascade on delete cascade,
    "title_workshop" varchar   not null,
    "date_workshop"  date not null,
    constraint "tr_client_workshop_pkey"
        primary key ("email_client", "title_workshop", "date_workshop"),
    constraint "fk_tr_client_workshop_workshop"
        foreign key ("title_workshop", "date_workshop") references workshop ("title", "date")
            on update cascade on delete cascade
);

alter table tr_client_workshop
    owner to "pi-group-6";

create index "fki_FK_TR_C_W_Client"
    on tr_client_workshop ("email_client");

create index "fki_FK_TR_C_W_Workshop"
    on tr_client_workshop ("title_workshop", "date_workshop");


-- ************************************************************************************************************
-- Start from here views are defined : ************************************************************************
-- ************************************************************************************************************

create view restricted_workshop as
	select "title", "date"
	from workshop;
