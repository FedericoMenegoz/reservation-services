CREATE SEQUENCE reservation_id_seq;

CREATE TABLE reservation (
                             id bigint PRIMARY KEY DEFAULT nextval('reservation_id_seq'),
                             itinerary_id varchar(50) NOT NULL,
                             contact_id bigint,
                             FOREIGN KEY (contact_id) REFERENCES contact(id)
);