import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import { ZonedDateTime } from "@internationalized/date";
import { useMutation } from "@tanstack/react-query";
import { useState } from "react";
import { addToast } from "@heroui/toast";

const toDisplayString = (start: ZonedDateTime, end: ZonedDateTime) => {
  const startDate = new Date(start.toAbsoluteString());
  const endDate = new Date(end.toAbsoluteString());

  const startDateFormatted = startDate.toLocaleDateString();
  const startTimeFormatted = endDate.toLocaleTimeString();
  const endTimeFormatted = endDate.toLocaleTimeString();

  return `${startDateFormatted} ${startTimeFormatted} - ${endTimeFormatted}`;
};

type RegisterAttendeeProps = {
  dateId: number;
  start: ZonedDateTime;
  end: ZonedDateTime;
  isEnabled: boolean;
};

export const RegisterAttendee = ({
  dateId,
  start,
  end,
  isEnabled,
}: RegisterAttendeeProps) => {
  const [email, setEmail] = useState("");

  const mutation = useMutation({
    mutationFn: (body: { dateId: number; email: string }) => {
      return fetch(`http://localhost:8888/appointment_date_attendee/`, {
        method: "POST",
        body: JSON.stringify(body),
      });
    },
  });

  const save = () => {
    if (email) {
      mutation.mutate(
        { dateId: dateId, email: email },
        {
          onSuccess: () => {
            addToast({
              title: "Ingeschreven!",
            });
          },
          onError: () => {
            addToast({
              title: "Error!",
            });
          },
        },
      );
    }
  };

  return (
    <div className="flex flex-row gap-4">
      <Input
        disabled={true}
        readOnly={true}
        value={toDisplayString(start, end)}
      />
      <Input
        isDisabled={!isEnabled}
        isReadOnly={!isEnabled}
        maxLength={200}
        placeholder={isEnabled ? "Email" : "Bezet"}
        value={email}
        onValueChange={setEmail}
      />
      <Button
        className="flex-1"
        color={"primary"}
        isDisabled={!isEnabled || !email?.length}
        onPress={save}
      >
        Inschrijven
      </Button>
    </div>
  );
};
