<<<<<<< HEAD
📌 Lost and Found Portal
📖 Overview

The Lost and Found Portal is a full-stack web application designed to streamline the reporting, searching, and claiming of lost and found items within a community (e.g., campus or city). The system ensures secure access and provides a personalized experience based on user roles (Admin, Reporter, User).

🚀 Features

🔑 JWT-based Authentication – Secure login and role-based access control.

📝 Lost Item Reporting – Users can report lost items with details.

📸 Found Item Posting – Users can post items they’ve found.

🤖 Smart Match Suggestions – Suggests possible matches between lost and found items.

📂 Claim Management – Users can claim found items; claims go through admin verification.

👤 Role-based Access:
   Admin: Manage all users’ personal details and oversee portal activity.
   Reporter: Manage reported items and user data related to reporting.
   User: Report lost items, post found items, and claim found items.

 📊 Admin Dashboard – View/manage users, lost items, found items, and claims

🛠️ Tech Stack

Frontend: Angular, HTML, CSS

Backend: Spring Boot, Java

Database: MySQL

API: REST API

📌 Modules

Lost Item Reporting – Report and track missing items.

Found Item Posting – Post and manage found items.

Smart Match Suggestions – Automatic suggestions for matching lost & found items.

Claim Management – Handle claims for found items with admin approval.

Security: JWT Authentication, Role-Based Access Control

## ⚙️ Installation / Setup Instructions

Follow these steps to set up and run the project on your local machine.

### 1. Clone the repository
```bash
git clone https://github.com/aminaminnu/LostAndFoundPortal.git
cd LostAndFoundPortal
`````
2. Backend Setup (Spring Boot)

 1.Navigate to the backend folder:
```bash
cd backend

`````
 2.Configure your database in application.properties (update username, password, and database name).

 3.Run the Spring Boot application:
```bash
mvn spring-boot:run
`````
or, if you use an IDE (IntelliJ / Eclipse / STS), simply run the MainProjectApplication.java.

3. Frontend Setup (Angular)

 1.Navigate to the frontend folder:
 ```bash
cd ../frontend
`````
 2.Install dependencies:
 ```bash
npm install
`````
 3.Start the Angular development server:
 ```bash
ng serve
`````
 4.Open your browser and go to:
 ```bash
http://localhost:4200
`````
🔧 Configuration

Database: MySQL (default)

Update application.properties in backend with your DB credentials:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/lostfound_db
spring.datasource.username=root
spring.datasource.password=yourpassword
`````

## Project Structure
```bash
LostAndFoundPortal/
├── backend/        # Spring Boot application
│   ├── src/        # Java source code
│   ├── pom.xml     # Maven dependencies
│
├── frontend/       # Angular application
│   ├── src/        # Angular source code
│   ├── package.json
│
└── README.md       # Project documentation
`````

## 📸 Screenshots  

### 🏠 Home Pages  
![Home](./screenshots/home.png)  
![Home1](./screenshots/home1.png)  
![Home2](./screenshots/home2.png)  
![Home3](./screenshots/home3.png)  
![Home4](./screenshots/home4.png)  

### 🔑 Login Page  
![Login](./screenshots/login.png)  

### 📝 Finder  
- Finder Form  
  ![Finder Form](./screenshots/finder-form.png)  
- Finder Home  
  ![Finder Home](./screenshots/finder-home.png)  
- Finder List  
  ![Finder List](./screenshots/finder-list.png)  

### 🛠️ Admin Dashboard  
![Admin](./screenshots/admin.png)  

### 🙍 Loser Pages  
- Loser Home  
  ![Loser Home](./screenshots/loser_home.png)  
- Loser Claim  
  ![Loser Claim](./screenshots/loser-claim.png)  
- Loser Form  
  ![Loser Form](./screenshots/loser-form.png)  

### 📂 Losing List  
![Losing List](./screenshots/losing_list.png)  

### 📝 Register Page  
![Register](./screenshots/register.png)  

### 📊 Reporter Dashboard  
![Reporter](./screenshots/reporter.png)  


👩‍💻 Author / Contact Info

Amina K A

📍 Thrissur, India







>>>>>>> e4e45d6 (Initial commit with Angular frontend ans spring Boot backend)
