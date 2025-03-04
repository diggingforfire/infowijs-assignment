package tech.luijben.infowijs

import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClient
import io.vertx.core.http.HttpClientRequest
import io.vertx.core.http.HttpClientResponse
import io.vertx.core.http.HttpMethod
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.CompletableFuture


@ExtendWith(VertxExtension::class)
class TestMainVerticle {

  @BeforeEach
  fun deploy_verticle(vertx: Vertx, testContext: VertxTestContext) {
    testContext.completeNow()
    val appointmentService = AppointmentServiceImpl(SqlClientFactory.createSqlClient(vertx))
    vertx.deployVerticle(MainVerticle(appointmentService)).onComplete(testContext.succeeding { _ -> testContext.completeNow() })
  }

  @Test
  fun test_missing_code_parameter(vertx: Vertx, testContext: VertxTestContext) {
    val httpClient: HttpClient = vertx.createHttpClient()
    httpClient.request(HttpMethod.GET, 8888, "localhost", "/appointment/")
      .compose { obj: HttpClientRequest -> obj.send() }
      .compose { res: HttpClientResponse -> Future.succeededFuture(res.statusCode())}
      .onComplete(testContext.succeeding { status ->
        Assertions.assertEquals(status, 405)
        testContext.completeNow()
      })

  }
}
