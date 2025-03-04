package tech.luijben.infowijs

import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import java.time.OffsetDateTime
import java.util.*

interface AppointmentService {
  fun getAppointmentDates(code: String) : Future<List<JsonObject>>
  fun addAppointmentAttendee(dateId: Int, email: String) : Future<Void>
  fun saveAppointment(title: String, description: String, dates: List<Pair<OffsetDateTime, OffsetDateTime>>) : Future<UUID>
}
