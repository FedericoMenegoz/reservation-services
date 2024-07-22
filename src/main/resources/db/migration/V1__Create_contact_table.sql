CREATE SEQUENCE contact_id_seq;

CREATE TABLE contact (
    id bigint PRIMARY KEY DEFAULT nextval('contact_id_seq'),
    email varchar(50) NOT NULL,
    telephone varchar(50) NOT NULL
);