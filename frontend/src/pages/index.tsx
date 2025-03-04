import { Link } from "@heroui/link";

import DefaultLayout from "@/layouts/default";

export default function IndexPage() {
  return (
    <DefaultLayout>
      <section className="flex flex-row items-center justify-center gap-4 py-8 md:py-10">
        <div>
        <Link
            className="flex justify-start items-center gap-1"
            color="foreground"
            href="/appointment/create"
          >
            Maak afspraak
          </Link>
          <Link
            className="flex justify-start items-center gap-1"
            color="foreground"
            href="/appointment/7c7dc7e9-200d-418a-9509-9e40952a64af"
          >
            Bekijk afspraak
          </Link>

        </div>
      </section>
    </DefaultLayout>
  );
}
