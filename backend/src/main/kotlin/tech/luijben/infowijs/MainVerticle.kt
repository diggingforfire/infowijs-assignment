package tech.luijben.infowijs

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler


class MainVerticle (private val appointmentService: AppointmentService) : AbstractVerticle () {

  override fun start() {
    val server: HttpServer = vertx.createHttpServer()
    val router = Router.router(vertx)

    router.route().handler(BodyHandler.create())

    router.route(HttpMethod.POST, "/appointment/").handler{ routingContext ->
      val response = routingContext.response()
      val json = routingContext.body().asJsonObject()
      response.end("done")
    }

    router.route(HttpMethod.GET, "/appointment/:code").handler { routingContext ->
      val code = routingContext.pathParam("code")
      val response = routingContext.response()

      appointmentService.getAppointmentDates(code)
        .onSuccess{ v ->
        response
            .putHeader("Content-Type", "application/json")
            .end(v.map { it.encode() }.toString());
      }.onFailure { error ->
          println(error.message)
          response.statusCode = 500
          response.end()
        }
    }

    server.requestHandler(router).listen(8888).onSuccess { _ ->
      println("HTTP server started on port 8888")
    }
  }
}
