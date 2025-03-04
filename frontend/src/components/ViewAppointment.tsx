import { useQuery } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import { parseAbsoluteToLocal } from "@internationalized/date";

import { Appointment } from "@/models/Appointment";
import { AppointmentResponseDetail } from "@/api/AppointmentResponse";
import { AppointmentDate } from "@/models/AppointmentDate";
export const ViewAppointment = () => {
  const params = useParams();
  const code = params["code"];

  type AppointmentData = {
    appointment: Appointment | null;
    dates: AppointmentDate[];
  };

  const { error, data, isPending } = useQuery({
    queryKey: ["appointment"],
    queryFn: async (): Promise<AppointmentData> => {
      const response = await fetch(`http://localhost:8888/appointment/${code}`);
      const appointmentResponseDetails: AppointmentResponseDetail[] =
        await response.json();
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
          }) as AppointmentDate,
      );

      debugger;

      return { appointment, dates };

      // if (!appointmentDetails.length) {
      //   return null;
      // }

      // //mapping wouldn't be necessary here if the API returned nicely structured json

      // const appointmentDates: Map<Number, AppointmentDate[]> =
      //   appointmentDetails.reduce((map, detail) => {
      //     const date = {
      //       start: parseAbsoluteToLocal(detail.date_start),
      //       end: parseAbsoluteToLocal(detail.date_end),
      //       attendees: [],
      //     } as AppointmentDate;

      //     if (!map.has(detail.date_id)) {
      //       map.set(detail.date_id, [date]);
      //     } else {
      //       map.get(detail.date_id)?.push(date);
      //     }

      //     return map;
      //   }, new Map<Number, AppointmentDate[]>());

      // const dates = [... appointmentDates.entries()];
      // debugger;
      // const appointment = {
      //   title: appointmentDetails[0].title,
      //   description: appointmentDetails[1].description,
      // } as Appointment;

      // return appointment;
    },
  });

  if (isPending) {
    return "Loading...";
  }

  if (error) {
    return "Error loading appointment";
  }

  return (
    <div>
      <div>{data.appointment?.title}</div>
      <div>{data.appointment?.description}</div>
    </div>
  );
};
