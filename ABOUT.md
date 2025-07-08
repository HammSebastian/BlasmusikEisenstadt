# About Blasmusik Eisenstadt

## Project Overview
Blasmusik Eisenstadt is a comprehensive web application designed to manage and showcase the activities of the Eisenstadt music organization. The platform serves both public visitors and organization members with different levels of access and functionality.

## Features

### Public Features
- View announcements and news
- Browse upcoming events and performances
- Learn about the organization
- Contact information and location details

### Member Features
- User authentication and authorization
- Profile management
- Event participation tracking
- Internal communications

## Technical Stack

### Backend
- **Framework**: Spring Boot
- **Security**: JWT Authentication
- **Database**: Relational Database (configured in application.properties)
- **Templating**: Thymeleaf for email templates
- **API Documentation**: OpenAPI/Swagger

### Frontend
- **Framework**: Angular
- **State Management**: RxJS
- **Styling**: CSS3
- **Type Safety**: TypeScript

## Project Structure
```
BlasmusikEisenstadt/
├── Backend/           # Spring Boot application
│   ├── src/
│   │   ├── main/java/at/sebastianhamm/backend/
│   │   │   ├── configuration/  # App configurations
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── models/         # Entity models
│   │   │   ├── repository/     # Data access layer
│   │   │   ├── services/       # Business logic
│   │   │   └── security/       # Security configurations
│   │   └── resources/          # Properties and templates
│   └── pom.xml                 # Maven configuration
│
└── Frontend/          # Angular application
    └── src/
        ├── app/
        │   ├── core/           # Core functionality
        │   ├── public/         # Public facing components
        │   └── shared/         # Shared modules and components
        └── assets/             # Static assets
```

## Getting Started

### Prerequisites
- Java 17+
- Node.js 16+
- Maven
- MySQL/PostgreSQL (or your preferred database)

### Installation
1. Clone the repository
2. Configure your database in `Backend/src/main/resources/application.properties`
3. Build the backend: `cd Backend && mvn clean install`
4. Install frontend dependencies: `cd Frontend && npm install`
5. Start the development servers

## License
This project is licensed under the [Creative Commons Zero (CC0) 1.0 Universal](LICENSE) license.
