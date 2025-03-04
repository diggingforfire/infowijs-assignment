import { Input, Textarea } from "@heroui/input";
import { useState } from "react";
import { Button } from "@heroui/button";
import { DatePicker } from "@heroui/date-picker";
import { getLocalTimeZone, now, ZonedDateTime } from "@internationalized/date";

import { title as header } from "@/components/primitives";
export const CreateAppointment = () => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [dateTimes, setDateTimes] = useState<ZonedDateTime[]>([
    now(getLocalTimeZone()),
  ]);

  const addDateTime = () => {
    setDateTimes([...dateTimes, now(getLocalTimeZone())]);
  };

  const save = () => {};

  return (
    <div className="flex flex-col gap-y-6">
      <span className={header()}>{"Nieuwe datumprikker"}</span>
      <Input
        maxLength={100}
        placeholder="Titel"
        value={title}
        onValueChange={setTitle}
      />
      <Textarea
        placeholder="Omschrijving"
        value={description}
        onValueChange={setDescription}
      />
      <div className="flex flex-row gap-4">
        <Button className="flex-1" color={"primary"} onPress={addDateTime}>
          Datum en tijd toevoegen
        </Button>

        <Button className="flex-1" color={"success"} onPress={save}>
          Opslaan
        </Button>
      </div>

      {dateTimes.map((dt) => (
        <DatePicker
          key={""}
          hideTimeZone
          showMonthAndYearPickers
          label="Voorgestelde datum en tijd"
          value={dt}
          variant="bordered"
        />
      ))}
    </div>
  );
};
