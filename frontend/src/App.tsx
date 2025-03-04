import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Route, Routes } from "react-router-dom";

import ViewAppointmentPage from "@/pages/view";
import CreateAppointmentPage from "@/pages/create";
import IndexPage from "@/pages/index";
const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Routes>
        <Route element={<IndexPage />} path="/" />
        <Route element={<ViewAppointmentPage />} path="/appointment/:code" />
        <Route element={<CreateAppointmentPage />} path="/appointment/create" />
      </Routes>
    </QueryClientProvider>
  );
}

export default App;
