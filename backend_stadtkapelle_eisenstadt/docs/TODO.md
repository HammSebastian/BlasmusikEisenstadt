# TODO List for Backend Stadtkapelle Eisenstadt

## High Priority

### Entity Design and Relationships
- [ ] Fix inconsistent bidirectional relationship between HistoryEntity and SectionsEntity (join column mismatch)
- [ ] Replace @Data with more specific Lombok annotations (@Getter, @Setter, etc.) on entities to avoid potential infinite recursion in toString() methods
- [ ] Standardize entity naming conventions (use singular form consistently, e.g., rename SectionsEntity to SectionEntity)
- [ ] Add proper validation constraints to entity fields (e.g., @NotNull, @Size, etc.)
- [ ] Fix database configuration inconsistency (spring.jpa.database=mysql vs PostgreSQL dialect)

### Service Layer
- [ ] Implement proper error handling in services with consistent patterns
- [ ] Extract file handling logic from EventServiceImpl to a dedicated FileStorageService
- [ ] Replace hardcoded paths and URLs in uploadImage method with configurable properties
- [ ] Add input validation in service methods beyond null checks
- [ ] Fix potential entity relationship management issues in GalleryServiceImpl (direct setting of collections)

### Security
- [ ] Secure actuator endpoints according to guidelines
- [ ] Consolidate duplicate CORS configuration
- [ ] Simplify security configuration by using more concise patterns for endpoint authorization
- [ ] Implement proper exception handling for security-related errors

## Medium Priority

### API Design
- [ ] Ensure consistent RESTful API patterns across all controllers
- [ ] Implement proper HTTP status codes in controllers (not always ResponseEntity.ok())
- [ ] Add OpenAPI documentation to all controllers and methods
- [ ] Implement consistent error response format across all endpoints
- [ ] Ensure API versioning is consistently applied across all endpoints

### Code Organization
- [ ] Create dedicated mapper classes for entity-to-DTO conversions
- [ ] Implement command objects for business operations as per guidelines
- [ ] Centralize exception handling with @ControllerAdvice
- [ ] Organize configuration with typed properties using @ConfigurationProperties

### Performance
- [ ] Add pagination for collection endpoints that may return large result sets
- [ ] Optimize lazy loading patterns to avoid N+1 query problems
- [ ] Add caching for frequently accessed, rarely changing data

## Low Priority

### Code Quality
- [ ] Add comprehensive JavaDoc to all public classes and methods
- [ ] Implement internationalization with ResourceBundles for user-facing messages
- [ ] Standardize naming conventions across the codebase
- [ ] Remove redundant code and consolidate similar functionality
- [ ] Add ordering to collection relationships where appropriate

### Testing
- [ ] Implement comprehensive unit tests for all service methods
- [ ] Add integration tests using Testcontainers
- [ ] Implement API tests for all endpoints
- [ ] Add test coverage reporting

### DevOps
- [ ] Configure proper logging with appropriate log levels
- [ ] Ensure sensitive data is not logged
- [ ] Add health checks for external dependencies
- [ ] Configure metrics collection for performance monitoring