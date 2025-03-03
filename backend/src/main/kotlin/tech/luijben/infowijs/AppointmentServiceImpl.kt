package tech.luijben.infowijs

import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.templates.SqlTemplate
import java.util.*

class AppointmentServiceImpl (val client: SqlClient) : AppointmentService {

  override fun getAppointmentDates(code: String) : Future<List<JsonObject>> {
    return SqlTemplate.forQuery(client, "" +
      "SELECT a.id as appointment_id, a.code, a.title, a.description, ad.start, ad.\"end\", ada.attendee_email\n" +
      "FROM appointment a\n" +
      "LEFT JOIN appointment_date ad on a.id = ad.appointment_id\n" +
      "LEFT JOIN appointment_date_attendee ada on ad.id = ada.appointment_date_id\n" +
      "WHERE a.code = #{code}")
      .mapTo(Row::toJson)
      .execute(Collections.singletonMap<String, Any>("code", code))
      .compose { row -> Future.succeededFuture(row.toList()) }
  }
}
