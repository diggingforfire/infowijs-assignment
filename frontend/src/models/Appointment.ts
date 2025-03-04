import { AppointmentDate } from "./AppointmentDate";

export interface Appointment {
  title: string;
  description: string;
  dates: AppointmentDate[];
}
