import { ZonedDateTime } from "@internationalized/date";

export interface AppointmentDate {
  id: number;
  start: ZonedDateTime;
  end: ZonedDateTime;
  attendees: string[];
}
