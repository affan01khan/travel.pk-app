import { Router } from "express";
import { destinations, packages } from "./data.js";
import { createEnquiry, findEnquiryById } from "./db.js";

export const api = Router();

api.get("/health", (_req, res) => {
  res.json({ status: "ok", service: "travelpk-api", timestamp: new Date().toISOString() });
});

api.get("/destinations", (_req, res) => {
  res.json({ data: destinations });
});

api.get("/destinations/:id", (req, res) => {
  const item = destinations.find((destination) => destination.id === req.params.id);
  if (!item) return res.status(404).json({ error: "Destination not found" });
  res.json({ data: item });
});

api.get("/packages", (_req, res) => {
  res.json({ data: packages });
});

api.get("/packages/:id", (req, res) => {
  const item = packages.find((travelPackage) => travelPackage.id === req.params.id);
  if (!item) return res.status(404).json({ error: "Package not found" });
  res.json({ data: item });
});

api.post("/enquiries", (req, res) => {
  const { name, email, phone = "", type = "general", message } = req.body ?? {};
  if (typeof name !== "string" || name.trim().length < 2 || name.length > 100) {
    return res.status(400).json({ error: "Name must be between 2 and 100 characters." });
  }
  if (typeof email !== "string" || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email) || email.length > 254) {
    return res.status(400).json({ error: "A valid email address is required." });
  }
  if (typeof phone !== "string" || phone.length > 30) {
    return res.status(400).json({ error: "Phone must be 30 characters or fewer." });
  }
  if (typeof type !== "string" || type.trim().length < 2 || type.length > 40) {
    return res.status(400).json({ error: "Enquiry type must be between 2 and 40 characters." });
  }
  if (typeof message !== "string" || message.trim().length < 5 || message.length > 4000) {
    return res.status(400).json({ error: "Message must be between 5 and 4000 characters." });
  }

  const result = createEnquiry.run({
    name: name.trim(),
    email: email.trim().toLowerCase(),
    phone: phone.trim(),
    enquiry_type: type.trim(),
    message: message.trim()
  });
  const saved = findEnquiryById.get(result.lastInsertRowid);
  return res.status(201).json({
    message: "Your enquiry has been received.",
    data: { id: saved.id, createdAt: saved.created_at }
  });
});

// Live fares and ticket issuance must come from an authorised Travel.pk or supplier API.
// This endpoint intentionally fails closed rather than returning invented availability.
api.post("/flights/search", (_req, res) => {
  if (!process.env.FLIGHT_PROVIDER_BASE_URL || !process.env.FLIGHT_PROVIDER_API_KEY) {
    return res.status(503).json({
      error: "Flight search is not connected.",
      detail: "Configure an authorised Travel.pk or airline supplier API before enabling live fares."
    });
  }
  return res.status(501).json({
    error: "Provider adapter not configured.",
    detail: "The provider's documented request and response format is required to complete this integration."
  });
});

// A booking lookup must never be simulated or guessed.
api.get("/trips/:bookingReference", (_req, res) => {
  return res.status(503).json({
    error: "Booking lookup is not connected.",
    detail: "A secure Travel.pk booking API integration is required to retrieve booking status."
  });
});
