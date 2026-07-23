# 🌿 BLOOM STORE — Advanced Java Mini Project
### Distributed E-Commerce System

---

## Tech Stack (Matches Assignment Requirements)

| Layer              | Technology         |
|--------------------|--------------------|
| Presentation       | JSP + Servlets     |
| Business Logic     | JavaBeans          |
| Persistence (ORM)  | Hibernate 5        |
| Distributed Layer  | Java RMI           |
| Database           | MySQL 8            |
| Build Tool         | Maven              |
| Server             | Apache Tomcat 9    |

---

## Project Structure

```
BloomStore/
├── pom.xml                             ← Maven build file
├── setup.sql                           ← Database seed script
└── src/main/
    ├── java/com/bloom/
    │   ├── model/
    │   │   ├── User.java               ← Hibernate entity
    │   │   ├── Product.java            ← Hibernate entity
    │   │   ├── Order.java              ← Hibernate entity
    │   │   └── CartItem.java           ← Hibernate entity
    │   ├── bean/
    │   │   ├── UserBean.java           ← JavaBean: auth logic
    │   │   └── CartBean.java           ← JavaBean: cart state
    │   ├── dao/
    │   │   ├── UserDAO.java            ← Hibernate CRUD
    │   │   ├── ProductDAO.java         ← Hibernate CRUD
    │   │   └── OrderDAO.java           ← Hibernate CRUD
    │   ├── servlet/
    │   │   ├── LoginServlet.java
    │   │   ├── RegisterServlet.java
    │   │   ├── ProductServlet.java
    │   │   ├── CartServlet.java
    │   │   ├── CheckoutServlet.java    ← Calls RMI
    │   │   ├── OrderServlet.java
    │   │   ├── AdminServlet.java
    │   │   └── LogoutServlet.java
    │   ├── rmi/
    │   │   ├── PaymentService.java     ← RMI Remote interface
    │   │   ├── PaymentServiceImpl.java ← RMI Server implementation
    │   │   ├── PaymentServer.java      ← RMI Registry starter
    │   │   └── PaymentResult.java      ← Serializable result
    │   └── util/
    │       └── HibernateUtil.java      ← SessionFactory singleton
    ├── resources/
    │   └── hibernate.cfg.xml           ← DB config
    └── webapp/
        ├── index.jsp                   ← Homepage
        ├── login.jsp
        ├── register.jsp
        ├── products.jsp
        ├── cart.jsp
        ├── checkout.jsp                ← Payment form
        ├── order-success.jsp
        ├── orders.jsp
        ├── css/
        │   └── bloom.css
        ├── admin/
        │   ├── dashboard.jsp
        │   └── manage-products.jsp
        └── WEB-INF/
            └── web.xml
```

---

## Setup Instructions (Step by Step)

### Prerequisites
- Java JDK 11+
- Maven 3.6+
- MySQL 8+
- Apache Tomcat 9+
- IDE: Eclipse / IntelliJ IDEA

---

### Step 1 — Clone / Import Project
- Open Eclipse → File → Import → Existing Maven Projects
- Select the `BloomStore` folder → Finish
- Wait for Maven to download dependencies

---

### Step 2 — Setup MySQL
```sql
-- Open MySQL Workbench or terminal:
mysql -u root -p

-- Create the database:
CREATE DATABASE bloom_store;

-- Hibernate will auto-create all tables on first run.
-- Then run seed data:
source /path/to/BloomStore/setup.sql;
```

---

### Step 3 — Configure Database Credentials
Open `src/main/resources/hibernate.cfg.xml` and update:
```xml
<property name="hibernate.connection.username">root</property>
<property name="hibernate.connection.password">YOUR_PASSWORD</property>
```

---

### Step 4 — Start the RMI Payment Server
```bash
# Compile and run PaymentServer BEFORE Tomcat:
mvn compile
cd target/classes
java com.bloom.rmi.PaymentServer
```
You should see:
```
✅ RMI Registry started on port 1099
✅ Bloom RMI Payment Server started
   Bound to: rmi://localhost/BloomPaymentService
```

---

### Step 5 — Deploy to Tomcat
- In Eclipse: Right-click project → Run As → Run on Server → Tomcat 9
- Or build WAR: `mvn package` → deploy `target/BloomStore.war` to Tomcat's `webapps/`

---

### Step 6 — Open in Browser
```
http://localhost:8080/BloomStore/
```

---

## Default Accounts (after seeding)

| Role  | Email              | Password  |
|-------|--------------------|-----------|
| Admin | admin@bloom.com    | bloom123  |
| User  | arjun@example.com  | bloom123  |

---

## Module Coverage (Assignment Requirements)

| Module           | Implemented In                          |
|------------------|-----------------------------------------|
| User Module      | Register/Login JSP + Servlets + UserBean|
| Product Module   | ProductServlet + products.jsp           |
| Payment (RMI)    | PaymentService + PaymentServiceImpl     |
| Order Management | CheckoutServlet + orders.jsp            |
| Admin Module     | AdminServlet + admin/dashboard.jsp      |
| Session Mgmt     | HttpSession in all Servlets             |
| Form Validation  | UserBean + JS in checkout.jsp           |
| Exception Handling | try/catch in all DAOs + Servlets      |
| Hibernate ORM    | All 4 entity classes + HibernateUtil    |
| RMI Distributed  | PaymentServer + CheckoutServlet lookup  |
