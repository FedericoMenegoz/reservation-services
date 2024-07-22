CREATE TABLE reservation_passengers (
    reservation_id bigint NOT NULL,
    passengers_id bigint NOT NULL,
    UNIQUE (passengers_id),
    FOREIGN KEY (passengers_id) REFERENCES passenger(id),
    FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);