# Ecommerce Automation (Without Database)

A full-stack web application that lets a seller:

1. Login with email and password.
2. Upload an Excel (`.xlsx`) containing products.
3. Preview products on UI.
4. Upload products to Amazon / Flipkart (simulated API flow).

## Tech Stack

- Backend: Spring Boot (Java 21)
- Frontend: React + Vite
- Storage: No database (in-memory session token + uploaded products)

## Login Credentials (Demo)

- Email: `seller@automation.com`
- Password: `Seller@123`

## Excel Format

First row headers must be exactly:

`id, product name, price, discount`

Example rows:

| id | product name | price | discount |
|----|--------------|-------|----------|
| P001 | Shoes | 2999 | 300 |
| P002 | Watch | 4999 | 500 |

## Run Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`.

## Run Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:5173`.

## APIs

- `POST /api/auth/login`
- `POST /api/products/upload-excel` (multipart: `file`, header `X-AUTH-TOKEN`)
- `POST /api/products/upload-marketplace` (body: `{ "marketplace": "Amazon" }`, header `X-AUTH-TOKEN`)

## Logging and Error Handling

- All key operations use SLF4J logs.
- Global exception handler returns consistent JSON error structure.
- Validation and Excel parsing errors return user-friendly messages.
