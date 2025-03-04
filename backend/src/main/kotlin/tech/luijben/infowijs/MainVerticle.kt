package tech.luijben.infowijs

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


class MainVerticle (private val appointmentService: AppointmentService) : AbstractVerticle () {

  override fun start() {
    val server: HttpServer = vertx.createHttpServer()
    val router = Router.router(vertx)

    router.route().handler(CorsHandler.create()
      .addOrigin("*")
      .allowedMethod(HttpMethod.GET)
      .allowedMethod(HttpMethod.POST)
      .allowedMethod(HttpMethod.OPTIONS)
      .allowedHeader("Access-Control-Request-Method")
      .allowedHeader("Access-Control-Allow-Credentials")
      .allowedHeader("Access-Control-Allow-Origin")
      .allowedHeader("Access-Control-Allow-Headers")
      .allowedHeader("Content-Type")
    )

    router.route().handler(BodyHandler.create())

    router.route(HttpMethod.POST, "/appointment/").handler { routingContext ->
      val response = routingContext.response()
      val json = routingContext.body().asJsonObject()

      val title = json.getString("title")
      val description = json.getString("description")
      val dates = json.getJsonArray("dates")
        .map { it as JsonObject }
        .map { Pair(
          OffsetDateTime.parse(it.getString("start"), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
          OffsetDateTime.parse(it.getString("end"), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        }

      appointmentService.saveAppointment(title, description, dates)
        .onSuccess { insertedCode ->
            response
              .putHeader("Content-Type", "application/json")
              .end("{ 'inserted_code': $insertedCode }")
        }.onFailure {
          response.setStatusCode(500).end()
        }
    }

    router.route(HttpMethod.POST, "/appointment_date_attendee/").handler{ routingContext ->
      val response = routingContext.response()
      val json = routingContext.body().asJsonObject()

      val email = json.getString("email")
      val dateId = json.getInteger("dateId")

      appointmentService.addAppointmentAttendee(dateId, email)
        .onSuccess {
          response.setStatusCode(200).end()
        }.onFailure {
          response.setStatusCode(500).end()
        }
    }

    router.route(HttpMethod.GET, "/appointment/:code").handler { routingContext ->
      val code = routingContext.pathParam("code")
      val response = routingContext.response()

      appointmentService.getAppointmentDates(code)
        .onSuccess{ dates ->
          response
            .putHeader("Content-Type", "application/json")
            .end(dates.map { it.encode() }.toString());
        }.onFailure { error ->
          println(error.message)
          response.setStatusCode(500).end()
        }
    }

    server.requestHandler(router).listen(8888).onSuccess { _ ->
      println("HTTP server started on port 8888")
    }
  }
}
