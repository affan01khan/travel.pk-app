import fs from "node:fs";
import path from "node:path";
import Database from "better-sqlite3";

const configuredPath = process.env.DATABASE_FILE || "./data/travelpk.sqlite";
const databasePath = path.resolve(process.cwd(), configuredPath);
fs.mkdirSync(path.dirname(databasePath), { recursive: true });

export const db = new Database(databasePath);
db.pragma("journal_mode = WAL");
db.pragma("foreign_keys = ON");

db.exec(`
  CREATE TABLE IF NOT EXISTS enquiries (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    phone TEXT,
    enquiry_type TEXT NOT NULL,
    message TEXT NOT NULL,
    created_at TEXT NOT NULL DEFAULT (datetime('now'))
  );

  CREATE INDEX IF NOT EXISTS idx_enquiries_created_at
  ON enquiries(created_at);
`);

export const createEnquiry = db.prepare(`
  INSERT INTO enquiries (name, email, phone, enquiry_type, message)
  VALUES (@name, @email, @phone, @enquiry_type, @message)
`);

export const findEnquiryById = db.prepare(`
  SELECT id, name, email, phone, enquiry_type, message, created_at
  FROM enquiries WHERE id = ?
`);
