import { setupServer } from "msw/node";
import { handlers } from "./handlers.js";

export const server = setupServer(...handlers);
export const baseUrl = "http://localhost:8080/api";
