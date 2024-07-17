package com.cherry.fm.reservationservices

fun main(){
    val server = Server(controller = arrayOf(ReservationController(), UiController(), InfoController()))
    server.startServer()
}