export interface AppointmentResponseDetail {
  id: number;
  code: string;
  title: string;
  description: string;
  date_id: number;
  date_start: string;
  date_end: string;
  date_attendee_id: number;
  date_attendee_email: string;
}
