# Spring Boot Razorpay Payment Gateway

A simple payment integration built with **Spring Boot** and **Razorpay**. It creates an order on the server, opens the Razorpay Checkout popup (UPI, Cards, Netbanking, Wallets), and verifies the payment signature on the server.

> Status: demo / learning project. Runs in Razorpay **Test Mode**.

## Features

- Server-side Razorpay order creation
- Razorpay Checkout popup (UPI, Cards, Netbanking, Wallets)
- Payment signature verification (HMAC SHA256)
- Layered structure: Controller -> Service -> Config
- Secrets loaded from environment variables

## Tech Stack

- Java 11+
- Spring Boot 2.7.x
- Razorpay Java SDK
- Lombok
- Bootstrap 5 + Razorpay Checkout.js (frontend)
- Maven

## Demo

### Checkout Page
![Checkout Page](src/main/resources/static/Screenshot%202026-10-18%20135029.png)

### Razorpay Payment Popup (UPI, Cards, EMI, Netbanking, Wallet, Pay Later)
![Razorpay Popup](src/main/resources/static/Screenshot%202026-10-18%20135010.png)

## Payment Flow

1. User clicks **Pay** on the checkout page.
2. Frontend calls `POST /pay/create-order`.
3. Server creates a Razorpay order (amount in paise) and returns `orderId`, `amount`, and the public `key`.
4. Checkout popup opens; user pays.
5. Razorpay returns `razorpay_payment_id`, `razorpay_order_id`, `razorpay_signature` to the frontend.
6. Frontend calls `POST /pay/verify`; server verifies the signature using the Key Secret.
7. On success, user is redirected to `/success`.

## Project Structure

```
src/main/java/com/example/payment
├── config
│   └── RazorpayConfig.java          # RazorpayClient bean
├── controller
│   └── RazorpayController.java      # /pay/create-order, /pay/verify, /success
├── service
│   ├── RazorpayService.java
│   └── RazorpayServiceImpl.java     # order creation + signature verification
└── entity
    └── Order.java

src/main/resources
├── static/index.html                # checkout page
└── application.properties
```

## Setup

### 1. Prerequisites

- JDK 11 or higher
- Maven
- A Razorpay account (Test Mode is enough)

### 2. Get API keys

1. Sign up at [razorpay.com](https://razorpay.com) and keep **Test Mode** on.
2. Go to `Account & Settings > API Keys > Generate Key`.
3. Copy the Key Id and Key Secret.

### 3. Configure

`src/main/resources/application.properties`:

```properties
razorpay.key.id=${RAZORPAY_KEY_ID}
razorpay.key.secret=${RAZORPAY_KEY_SECRET}
```

Set the environment variables before running:

```bash
# Linux / macOS
export RAZORPAY_KEY_ID=rzp_test_xxxxxxxx
export RAZORPAY_KEY_SECRET=xxxxxxxx

# Windows (PowerShell)
$env:RAZORPAY_KEY_ID="rzp_test_xxxxxxxx"
$env:RAZORPAY_KEY_SECRET="xxxxxxxx"
```

In IntelliJ: `Run > Edit Configurations > Environment variables`.

### 4. Run

```bash
mvn clean install
mvn spring-boot:run
```

Open **http://localhost:8080** (do not open `index.html` through the IDE preview server, the API calls will fail).

## API Endpoints

| Method | Endpoint            | Description                                  |
|--------|---------------------|----------------------------------------------|
| POST   | `/pay/create-order` | Creates a Razorpay order, returns order data |
| POST   | `/pay/verify`       | Verifies payment signature                   |
| GET    | `/success`          | Success page                                 |

## Testing (Test Mode)

No real money is charged in Test Mode.

| Method     | Details                                                      |
|------------|--------------------------------------------------------------|
| Card       | `4111 1111 1111 1111`, any future expiry, any CVV, OTP `1234` |
| UPI (VPA)  | `success@razorpay` (success), `failure@razorpay` (failure)   |
| Netbanking | Pick any bank and choose "Success"                           |

Note: the UPI QR code does not work with real UPI apps in Test Mode. Use the UPI ID option instead.

## Going Live

1. Complete KYC and activate the account on the Razorpay dashboard.
2. Switch to Live Mode and generate `rzp_live_...` keys.
3. Replace the environment variables with the live keys.
4. Test with a small amount (for example Rs 1) first.

## Security Notes

- Never commit the Key Secret to Git. Use environment variables.
- Only the Key Id is sent to the frontend.
- Always verify the signature on the server before marking a payment as successful.
- Amount is decided on the server, never trust the amount from the client.

## Future Improvements

- Save orders and payment status in a database (CREATED / PAID / FAILED)
- Razorpay webhook (`payment.captured`) for reliable confirmation
- User authentication (Spring Security + JWT) and order history
- Refund API and failed-payment handling
- Dynamic cart / product pricing

## Author

**Rocky**
Java Full Stack Developer | MCA Graduate

- Skills: Java, Spring Boot, REST APIs, JWT, Hibernate, MySQL, Docker, AWS
- GitHub: [your-github-username](https://github.com/your-github-username)
- LinkedIn: [your-linkedin-profile](https://www.linkedin.com/in/your-linkedin-profile)
- Email: your-email@example.com

## License

This project is for learning purposes.
