# Requirements for the backend project

### Configuration
<hr>
- Create a .env file in the root directory of the project
- Add the following environment variables to the .env file
- DATABASE_URL=postgresql://postgres:postgres@localhost:5432/kapelle-eisenstadt
- JWT_SECRET=your-secret-key

### Database
<hr>
- Create a database named "kapelle-eisenstadt" in a PostgreSQL server
- Run the migrations in the migrations directory

### Docker
<hr>
- Build the Docker image: docker build -t kapelle-eisenstadt .
- Run the Docker container: docker run -p 8080:8080 kapelle-eisenstadt

### Testing
<hr>
- Run the tests: mvn test

### Documentation
<hr>
- Generate the API documentation: mvn springdoc:generate
- Access the API documentation: http://localhost:8080/swagger-ui/index.html

### Deployment
<hr>
- Build the Docker image: docker build -t kapelle-eisenstadt .
- Run the Docker container: docker run -p 8080:8080 kapelle-eisenstadt

### Security
<hr>
- Use a strong and secure password for the JWT_SECRET environment variable
- Use a strong and secure secret key for the JWT_SECRET environment variable
