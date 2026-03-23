# Super Market Billing System

Console-based Java monolith application for supermarket operations with role-based login for Admin and Customer.

## Modules Covered

- Module A: Authentication and role-based welcome menu
- Module B: Inventory and user management (Admin)
- Module C: Customer purchase and cart operations
- Module D: Credit-based payment, cashback, loyalty points and coupon reward
- Module E: Purchase history by bill date and bill number
- Module F: Admin reports (stock, unsold products, top customers, top admins by sales)

## Default Login Credentials

### Admin Users
- Email: `admin@mart.com`
- Password: `admin123`

- Email: `admin2@mart.com`
- Password: `admin123`

### Customer Users
- Email: `c1@mart.com`
- Password: `pass123`

- Email: `c2@mart.com`
- Password: `pass123`

## Business Rules Implemented

- Every new customer gets initial preloaded credit of Rs 1000.
- Customer can purchase only within available credit balance.
- Reward rule A:
  - If bill payable is >= Rs 5000, customer gets Rs 100 added back to wallet.
  - No loyalty points are added for this bill.
- Reward rule B:
  - For every Rs 100 payable, 1 loyalty point is added.
  - For every 50 points accumulated, one Rs 100 discount coupon is generated.
  - Coupon is auto-applied on next bill.

## Project Structure

```
super-market-billing-system/
├── README.md
└── src/
    ├── data/
    │   └── DataStore.java
    ├── model/
    │   ├── Bill.java
    │   ├── BillItem.java
    │   ├── CartItem.java
    │   ├── Product.java
    │   ├── Role.java
    │   └── User.java
    ├── repository/
    │   ├── BillRepository.java
    │   ├── ProductRepository.java
    │   └── UserRepository.java
    ├── service/
    │   ├── AuthService.java
    │   ├── BillingService.java
    │   ├── CartService.java
    │   ├── InventoryService.java
    │   └── ReportService.java
    └── ui/
        └── App.java
```

## Functional Overview

### Admin Features

- Add product
- Modify product details and quantity
- Delete product
- View products sorted by name
- View products sorted by price
- Search products by name
- Add users (Admin or Customer)
- Increase customer credit
- Reports:
  - Products with low quantity
  - Products never bought
  - Customers with highest purchase value
  - Admins with highest sales value

### Customer Features

- View all products
- Add product to cart by product ID and quantity
- Add same product again (quantity increases)
- Edit cart item quantity
- Delete item from cart
- Proceed to payment
- View purchase history (bill number and date)

## Build and Run

From workspace root:

```bash
cd /home/kishorekannann/projects/consoleApplication/super-market-billing-system
javac -d out src/model/*.java src/data/*.java src/repository/*.java src/service/*.java src/ui/*.java
java -cp out ui.App
```

## Notes on Sales Attribution

To support the report "admins who made more sale", checkout asks for a billing admin ID. The selected admin gets credited for that bill's sale value.

## Suggested Real-time Enhancements

- Product categories and GST per product
- Bill cancel and refund flow
- Expiry date tracking for perishables
- Daily/monthly sales dashboard
- Persistent storage using file/DB instead of in-memory data
- Coupon validity period and promo engine
