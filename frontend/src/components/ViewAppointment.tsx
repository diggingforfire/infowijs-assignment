import { useQuery } from "@tanstack/react-query";
import { useParams } from "react-router-dom";

import { uglyMap } from "./util";
import { RegisterAttendee } from "./RegisterAttendee";

import { Appointment } from "@/models/Appointment";
import { AppointmentResponseDetail } from "@/api/AppointmentResponse";
import { AppointmentDate } from "@/models/AppointmentDate";
import { title, subtitle } from "@/components/primitives";

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

      return uglyMap(appointmentResponseDetails);
    },
  });

  if (isPending) {
    return "Loading...";
  }

  if (error) {
    return "Error loading appointment";
  }

  return (
    <>
      <div className="flex flex-col justify-center mb-10">
        <div className={`${title()} text-center`}>
          {data.appointment?.title}
        </div>
        <div className={`${subtitle()} text-center`}>
          {data.appointment?.description}
        </div>
      </div>
      <div className="flex flex-col gap-5">
        {data.dates.map((dateTime) => (
          <div key={dateTime.id}>
            <RegisterAttendee
              dateId={dateTime.id}
              end={dateTime.end}
              isEnabled={!dateTime.attendees.length}
              start={dateTime.start}
            />
          </div>
        ))}
      </div>
    </>
  );
};
