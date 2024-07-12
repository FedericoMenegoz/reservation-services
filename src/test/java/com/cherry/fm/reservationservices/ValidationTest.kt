package com.cherry.fm.reservationservices

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.helidon.common.media.type.MediaTypes
import io.helidon.http.media.jackson.JacksonSupport
import io.helidon.webclient.api.WebClient
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.function.Consumer

class ValidationTest{
	private fun webClient(): WebClient? = WebClient.builder()
	.mediaContext(Consumer {
		it.addMediaSupport(JacksonSupport.create(ObjectMapper().apply {
			registerModule(JavaTimeModule())
			configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
		}))
	})
	.build()

	@Test
	fun `value classes parsing with invalid firstname test`(){
		val server: Server = Server(port= 8081, controller = ReservationController())
		server.startServer()
		val client = webClient()!!

		assertEquals("{\"code\":400,\"shortDescription\":\"Validation error\",\"reason\":\"400 Bad Request\"}",client
			.post()
			.uri("http://localhost:" + server.port)
			.path("/api/flights/reservation")
			.contentType(MediaTypes.APPLICATION_JSON)
			.submit("""
			{
			   "passengers":[
			      {
			         "type":"ADULT",
			         "gender":"MALE",
			         "birth":"1980-01-12",
			         "nationality":"ARG",
			         "firstName":"r",
			         "lastName":"r",
			         "document":{
			            "number":"20123456783",
			            "type":"PASSPORT",
			            "expiration":"2027-07-21"
			         }
			      }
			   ],
			   "contact":{
			      "telephone":"4444-1234",
			      "email":"flights@manning.com"
			   },
			   "itineraryId":"f2f61e3c-fb26-49bc-bf0f-48eef2d3d6a5*c9f178d1-3583-4598-9194-c28a29c269de"
			}
		""".trimIndent()).inputStream().bufferedReader().readText())
	}
@Test
fun `value classes parsing with invalid lastname test`(){
		val server: Server = Server(controller = ReservationController())
		server.startServer()
		val client = webClient()!!

		assertEquals("{\"code\":400,\"shortDescription\":\"Validation error\",\"reason\":\"400 Bad Request\"}",client
			.post()
			.uri("http://localhost:" + server.port)
			.path("/api/flights/reservation")
			.contentType(MediaTypes.APPLICATION_JSON)
			.submit("""
			{
			   "passengers":[
			      {
			         "type":"ADULT",
			         "gender":"MALE",
			         "birth":"1980-01-12",
			         "nationality":"ARG",
			         "firstName":"Andres",
			         "lastName":"r",
			         "document":{
			            "number":"20123456783",
			            "type":"PASSPORT",
			            "expiration":"2027-07-21"
			         }
			      }
			   ],
			   "contact":{
			      "telephone":"4444-1234",
			      "email":"flights@manning.com"
			   },
			   "itineraryId":"f2f61e3c-fb26-49bc-bf0f-48eef2d3d6a5*c9f178d1-3583-4598-9194-c28a29c269de"
			}
		""".trimIndent()).inputStream().bufferedReader().readText())
	}
@Test
fun `bad format test`(){
		val server: Server = Server(controller = ReservationController())
		server.startServer()
		val client = webClient()!!

		assertEquals("{\"code\":400,\"shortDescription\":\"JSON format error\",\"reason\":\"400 Bad Request\"}",client
			.post()
			.uri("http://localhost:" + server.port)
			.path("/api/flights/reservation")
			.contentType(MediaTypes.APPLICATION_JSON)
			.submit("{bad format}").inputStream().bufferedReader().readText())
	}
}