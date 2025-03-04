import { parseAbsoluteToLocal } from "@internationalized/date";

import { AppointmentResponseDetail } from "@/api/AppointmentResponse";
import { Appointment } from "@/models/Appointment";
import { AppointmentDate } from "@/models/AppointmentDate";

type AppointmentData = {
  appointment: Appointment | null;
  dates: AppointmentDate[];
};

export const uglyMap = (
  appointmentResponseDetails: AppointmentResponseDetail[],
): AppointmentData => {
  const appointment = appointmentResponseDetails.length
    ? ({
        title: appointmentResponseDetails[0].title,
        description: appointmentResponseDetails[0].description,
      } as Appointment)
    : null;

  const appointmentDates: Map<Number, AppointmentDate[]> =
    appointmentResponseDetails.reduce((map, detail) => {
      const date = {
        id: detail.date_id,
        start: parseAbsoluteToLocal(detail.date_start),
        end: parseAbsoluteToLocal(detail.date_end),
        attendees: detail.date_attendee_email
          ? [detail.date_attendee_email]
          : [],
      } as AppointmentDate;

      if (!map.has(detail.date_id)) {
        map.set(detail.date_id, [date]);
      } else {
        map.get(detail.date_id)?.push(date);
      }

      return map;
    }, new Map<Number, AppointmentDate[]>());

  const dates: AppointmentDate[] = [...appointmentDates.entries()].map(
    (e) =>
      ({
        id: e[0],
        attendees: e[1].flatMap((d) => d.attendees),
        start: e[1][0].start,
        end: e[1][0].end,
      }) as AppointmentDate,
  );

  return {
    appointment: appointment,
    dates: dates,
  } as AppointmentData;
};
