CREATE SEQUENCE passenger_id_seq;

CREATE TABLE passenger (
   id bigint PRIMARY KEY DEFAULT nextval('passenger_id_seq'),
   birth date NOT NULL,
   first_name varchar(20) NOT NULL,
   gender VARCHAR(20) NOT NULL CHECK (gender IN ('MALE', 'FEMALE', 'NOT_SPECIFY')),
   last_name varchar(20) NOT NULL,
   nationality varchar(3) NOT NULL,
   type VARCHAR(20) NOT NULL CHECK (type IN ('ADULT', 'CHILDREN', 'INFANT')),
   document_id bigint,
   FOREIGN KEY (document_id) REFERENCES document(id)
);