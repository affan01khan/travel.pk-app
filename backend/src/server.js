import "dotenv/config";
import express from "express";
import cors from "cors";
import helmet from "helmet";
import rateLimit from "express-rate-limit";
import { api } from "./routes.js";

const app = express();
const port = Number(process.env.PORT || 8080);
const allowedOrigins = (process.env.CORS_ORIGIN || "*").split(",").map((value) => value.trim());

app.disable("x-powered-by");
app.use(helmet());
app.use(cors({
  origin(origin, callback) {
    if (!origin || allowedOrigins.includes("*") || allowedOrigins.includes(origin)) {
      return callback(null, true);
    }
    return callback(new Error("Origin is not allowed by CORS"));
  }
}));
app.use(express.json({ limit: "32kb" }));
app.use("/api", rateLimit({
  windowMs: 15 * 60 * 1000,
  limit: 120,
  standardHeaders: "draft-7",
  legacyHeaders: false
}));
app.use("/api", api);

app.use((req, res) => res.status(404).json({ error: "Route not found" }));
app.use((error, _req, res, _next) => {
  console.error(error);
  if (error.message === "Origin is not allowed by CORS") {
    return res.status(403).json({ error: "Origin not allowed" });
  }
  return res.status(500).json({ error: "Internal server error" });
});

app.listen(port, "0.0.0.0", () => {
  console.log(`Travel.pk API listening on port ${port}`);
});
