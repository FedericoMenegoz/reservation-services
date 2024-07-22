CREATE SEQUENCE document_id_seq;

CREATE TABLE document (
    id bigint PRIMARY KEY DEFAULT nextval('document_id_seq'),
    expiration date NOT NULL,
    number varchar(20) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('PASSPORT', 'NATIONAL'))
);