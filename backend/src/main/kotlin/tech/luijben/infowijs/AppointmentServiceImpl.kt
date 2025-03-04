package tech.luijben.infowijs

import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.templates.SqlTemplate
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.util.*


class AppointmentServiceImpl (private val client: SqlClient) : AppointmentService {

  override fun getAppointmentDates(code: String) : Future<List<JsonObject>> {
    // NOTE: this function should return nicely structured entities
    return SqlTemplate.forQuery(client,
      "SELECT a.id, a.code, a.title, a.description, " +
      "ad.id as date_id, ad.start as date_start, ad.\"end\" as date_end, " +
      "ada.id as date_attendee_id, ada.attendee_email as date_attendee_email\n" +
      "FROM appointment a\n" +
      "LEFT JOIN appointment_date ad on a.id = ad.appointment_id\n" +
      "LEFT JOIN appointment_date_attendee ada on ad.id = ada.appointment_date_id\n" +
      "WHERE a.code = #{code}")
      .mapTo(Row::toJson)
      .execute(Collections.singletonMap<String, Any>("code", code))
      .compose { row -> Future.succeededFuture(row.toList()) }
  }

  override fun addAppointmentAttendee(dateId: Int, email: String) : Future<Void> {
    val parameters: Map<String, Any> = mapOf("appointment_date_id" to dateId, "date_attendee_email" to email)
    return SqlTemplate
      .forUpdate(client,
        "INSERT INTO appointment_date_attendee (appointment_date_id, attendee_email) " +
        "VALUES (#{appointment_date_id},#{date_attendee_email})")
      .execute(parameters)
      .compose { Future.succeededFuture()}
  }


  override fun saveAppointment(title: String, description: String, dates: List<Pair<OffsetDateTime, OffsetDateTime>>) : Future<UUID> {
    val parameters: Map<String, Any> = mapOf("title" to title, "description" to description)
    return SqlTemplate
      .forQuery(client,
        "INSERT INTO appointment (title, description) " +
        "VALUES (#{title},#{description})" +
        "RETURNING id, code")
      .execute(parameters)
      .compose { row ->

        val insertedId = row.first().getInteger("id")
        val insertedCode = row.first().getUUID("code")

        // NOTE: all inserts in this function should happen inside of a transaction
        for (datePair in dates) {
          val parameters = mapOf("appointment_id" to insertedId, "start" to datePair.first, "end" to datePair.second)

          SqlTemplate
            .forQuery(client,
              "INSERT INTO appointment_date (appointment_id, start, \"end\") " +
                "VALUES (#{appointment_id},#{start},#{end})")
            .execute(parameters)
        }

        Future.succeededFuture(insertedCode)
      }
  }
}
