package tech.luijben.infowijs

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.pgclient.PgBuilder
import io.vertx.pgclient.PgConnectOptions
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient

fun main(args: Array<String>) {
  val vertx = Vertx.vertx()

  // old school manual dependency injection
  val appointmentService = AppointmentServiceImpl(createSqlClient(vertx))

  val mainVerticle = MainVerticle(appointmentService)
  val options = DeploymentOptions()
  vertx.deployVerticle(mainVerticle, options)

}

private fun createSqlClient(vertx: Vertx): SqlClient {

  // these should come from a proper source like environment variables
  val connectOptions: PgConnectOptions = PgConnectOptions()
    .setPort(5432)
    .setHost("localhost")
    .setDatabase("infowijs")
    .setUser("infowijs")
    .setPassword("infowijs-secret")

  val poolOptions: PoolOptions = PoolOptions()
    .setMaxSize(5)

  val client = PgBuilder
    .client()
    .with(poolOptions)
    .connectingTo(connectOptions)
    .using(vertx)
    .build()

  return client
}
