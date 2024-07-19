package com.cherry.fm.reservationservices

import com.cherry.fm.reservationservices.data.contact.ContactRepository
import com.cherry.fm.reservationservices.data.document.DocumentRepository
import com.cherry.fm.reservationservices.data.passenger.PassengerRepository
import com.cherry.fm.reservationservices.data.reservation.ReservationRepository
import com.cherry.fm.reservationservices.data.reservation_passengers.ReservationPassengersRepository
import com.cherry.fm.reservationservices.services.ReservationService
import io.helidon.config.Config
import io.helidon.dbclient.DbClient

fun main(){
    val conf: Config = Config.create()
    val dbClient = DbClient.create(conf["db"])
    val service = ReservationService(
        repoContact = ContactRepository(dbClient),
        repoDocument = DocumentRepository(dbClient),
        repoPassenger = PassengerRepository(dbClient),
        repoReservation = ReservationRepository(dbClient),
        repoReservationPassenger = ReservationPassengersRepository(dbClient)
    )
    val server = Server(controller = arrayOf(ReservationController(resService = service), UiController(), InfoController()))
    server.startServer()
}