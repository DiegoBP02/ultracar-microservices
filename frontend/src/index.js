import React from "react";
import ReactDOM from "react-dom/client";
import { ChakraProvider } from "@chakra-ui/react";
import { createStandaloneToast } from "@chakra-ui/toast";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./index.css";
import Login from "./components/login/Login";
import Dashboard from "./components/dashboard/Dashboard";
import AuthProvider from "./context/AuthContext";
import ProtectedRoute from "./components/shared/ProtectedRoute";
import OrderOfService from "./components/orderOfService/OrderOfService";
import OrderOfServicePDFGenerator from "./pdf/OrderOfServicePDFGenerator";

const { ToastContainer } = createStandaloneToast();

const router = createBrowserRouter([
  {
    path: "/",
    element: <Login />,
  },
  {
    path: "/dashboard",
    element: (
      <ProtectedRoute>
        <Dashboard />
      </ProtectedRoute>
    ),
  },
  {
    path: "/orderOfService/:vehicleId",
    element: (
      <ProtectedRoute>
        <OrderOfService />
      </ProtectedRoute>
    ),
  },
  {
    path: "/generatePdf/:orderOfServiceId",
    element: (
      <ProtectedRoute>
        <OrderOfServicePDFGenerator />
      </ProtectedRoute>
    ),
  },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <ChakraProvider>
      <AuthProvider>
        <RouterProvider router={router} />
      </AuthProvider>
      <ToastContainer />
    </ChakraProvider>
  </React.StrictMode>
);
