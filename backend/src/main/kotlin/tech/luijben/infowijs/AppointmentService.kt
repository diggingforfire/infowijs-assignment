package tech.luijben.infowijs

import io.vertx.core.Future
import io.vertx.core.json.JsonObject

interface AppointmentService {
  fun getAppointmentDates(code: String) : Future<List<JsonObject>>
}
