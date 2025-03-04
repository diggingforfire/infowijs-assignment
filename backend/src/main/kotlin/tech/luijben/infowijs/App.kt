package tech.luijben.infowijs

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx

fun main(args: Array<String>) {
  val vertx = Vertx.vertx()

  // NOTE: old school manual dependency injection
  val appointmentService = AppointmentServiceImpl(SqlClientFactory.createSqlClient(vertx))

  val mainVerticle = MainVerticle(appointmentService)
  val options = DeploymentOptions()
  vertx.deployVerticle(mainVerticle, options)
}
