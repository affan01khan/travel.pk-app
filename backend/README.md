# Travel.pk Backend API

Node.js + Express REST API for the Travel.pk Android app.

## Included
- Health endpoint
- Destination and sample travel-package endpoints
- Enquiry submission stored in SQLite
- Request validation, rate limiting, security headers and CORS
- Explicitly disabled live flight search and booking lookup until authorised provider APIs are supplied

## Run locally
Requires Node.js 20 or newer.

```bash
cd backend
cp .env.example .env
npm install
npm run dev
```

API base URL: `http://localhost:8080/api`

## Endpoints
- `GET /api/health`
- `GET /api/destinations`
- `GET /api/destinations/:id`
- `GET /api/packages`
- `GET /api/packages/:id`
- `POST /api/enquiries`
- `POST /api/flights/search` (returns HTTP 503 until an authorised provider adapter is implemented)
- `GET /api/trips/:bookingReference` (returns HTTP 503 until secure booking lookup is integrated)

Example enquiry:
```json
{
  "name": "Ayesha Khan",
  "email": "ayesha@example.com",
  "phone": "+92 300 1234567",
  "type": "package",
  "message": "Please send a quote for a Hunza trip in December."
}
```

## Android integration
Set the Android app's API base URL to the deployed HTTPS address ending in `/api/`. Do not use `localhost` from a physical phone; localhost refers to the phone itself. The Android app currently contains native screens but does not yet call this backend. Wire an HTTP client to these endpoints before presenting server-backed content.

## Production checklist
- Deploy behind HTTPS.
- Set `CORS_ORIGIN` to the exact allowed app/web origins.
- Use persistent storage and automated backups for the SQLite database, or migrate to managed PostgreSQL for multi-instance hosting.
- Add authentication and access control before exposing private trip records or staff enquiry dashboards.
- Configure authorised Travel.pk flight, booking and payment provider APIs. Never store card data or provider secrets in the Android app.
- Add privacy notice, retention policy, monitoring and operational alerting before collecting real customer data.
