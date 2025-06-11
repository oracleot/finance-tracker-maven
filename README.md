# Personal Finance Tracker

A Java Spring Boot application for tracking personal income and expenses. This application helps you manage your finances by allowing you to record transactions, categorize them, and view financial summaries.

## Features

- 💰 Track income and expenses
- 📊 View financial summaries and current balance
- 🔄 Set up recurring transactions
- 📝 Categorize transactions
- 🔍 Smart duplicate detection
- 📱 Responsive design for mobile and desktop
- 📅 Transaction history with filtering options

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.6 or higher
- PostgreSQL (for production) / H2 Database (included for development)

## Quick Start

1. Clone the repository:
```bash
git clone https://github.com/oracleot/finance-tracker-maven.git
cd finance-tracker-maven
```

2. Build the application:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. Open your browser and navigate to:
```
http://localhost:8080
```

## Development Setup

### Database Configuration

The application uses H2 Database for development by default. To switch to PostgreSQL:

1. Update `application.properties` with your PostgreSQL credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/financedb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

2. Ensure PostgreSQL driver dependency is active in `pom.xml` (already included)

### Running Tests

```bash
mvn test
```

### Available Profiles

- `dev` - Development profile with H2 Database (default)
- `prod` - Production profile with PostgreSQL

To run with a specific profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Usage Guide

### Adding Transactions

1. Click "Add Transaction" on the dashboard
2. Fill in the transaction details:
   - Description
   - Amount
   - Type (Income/Expense)
   - Category
   - Date
3. For recurring transactions:
   - Select "Recurring Transaction"
   - Choose frequency (Weekly/Monthly/Yearly)
   - Set end date (optional)

### Managing Transactions

- View all transactions on the Transactions page
- Filter transactions by date range or category
- Delete transactions as needed
- The system will warn about potential duplicate transactions

### Dashboard Features

- Total Income
- Total Expenses
- Current Balance
- Recent Transactions
- Category-wise breakdown

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/financetracker/
│   │       ├── controller/    # MVC Controllers
│   │       ├── model/         # Domain Models
│   │       ├── repository/    # Data Access Layer
│   │       └── service/       # Business Logic
│   └── resources/
│       └── templates/         # Thymeleaf Templates
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
- [Maven](https://maven.apache.org/) - Dependency Management
- [Thymeleaf](https://www.thymeleaf.org/) - Template Engine
- [H2 Database](https://www.h2database.com/) - Development Database
- [PostgreSQL](https://www.postgresql.org/) - Production Database

## License

This project is licensed under the MIT License - see the LICENSE file for details

## Acknowledgments

- Spring Boot team for the excellent framework
- The open-source community for inspiration and resources
