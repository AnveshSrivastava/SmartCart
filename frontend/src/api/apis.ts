import type { SignInRequest } from "@/types/auth";
import axios from "axios";

const api = axios.create({ baseURL: "http://localhost:8080" });

api.interceptors.request.use((config) => {
  if (config.url?.startsWith("/api/v1/auth")) {
    return config;
  }

  const userSession = localStorage.getItem("userSession");
  if (userSession) {
    const { token } = JSON.parse(userSession);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  }
  return config;
});

export const signIn = (signInRequest: SignInRequest) =>
  api.post("/api/v1/auth/login", signInRequest);

export const getUserData = () => api.get("/api/v1/users/me");
