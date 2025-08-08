# Production Readiness TODO List

This document outlines all the necessary changes to make the Stadtkapelle Eisenstadt Backend application production-ready according to Spring Boot best practices and the provided guidelines.

## 🔧 Structural Improvements

### 7. Create Dedicated Mapper Classes
**Priority: MEDIUM**
- **Issue**: Manual mapping in service classes
- **Actions**:
  - Create mapper classes in `domain/mappers/` package for each module
  - Move mapping logic from services to dedicated mappers
  - Consider using MapStruct for complex mappings

### 8. Enhance Global Exception Handling
**Priority: MEDIUM**
- **Issue**: Only generic exception handler exists
- **Actions**:
  - Add specific handlers for `MethodArgumentNotValidException`
  - Add handlers for `AccessDeniedException`
  - Add handlers for `DataIntegrityViolationException`
  - Add handlers for custom business exceptions
  - Ensure consistent `ErrorResponse` DTO structure

## 🛡️ Security & Configuration Enhancements

## 📦 Dependency Management

### 11. Add Missing Dependencies
**Priority: LOW**
- **Actions**:
  - Add Flyway: `org.flywaydb:flyway-core`
  - Add Testcontainers: `org.testcontainers:testcontainers-bom`
  - Add AssertJ (should be included in spring-boot-starter-test)
  - Add WebJars for static content if needed
  - Remove Lombok dependency

### 12. Update Dependencies
**Priority: LOW**
- **Actions**:
  - Review all dependency versions for security updates
  - Ensure all dependencies are compatible with Spring Boot 3.5.4
  - Add dependency management for consistent versions

## 🚀 Performance & Production Optimizations

### 13. Database Optimizations
**Priority: LOW**
- **Actions**:
  - Review JPA entity relationships and lazy loading
  - Add database indexes through Flyway migrations
  - Configure connection pooling for production
  - Add database health checks

### 14. Caching Strategy
**Priority: LOW**
- **Actions**:
  - Evaluate need for caching (Redis, Caffeine)
  - Implement caching for frequently accessed data
  - Configure cache eviction strategies

### 15. API Documentation
**Priority: LOW**
- **Actions**:
  - Review and enhance OpenAPI documentation
  - Add comprehensive examples to API documentation
  - Configure Swagger UI for different environments

## 📋 Implementation Priority

1. **Phase 1 (Critical)**: Items 1-4 (Remove Lombok, Flyway, Production Config, Testing)
2. **Phase 2 (Important)**: Items 5-8 (Package Structure, Typed Config, Mappers, Exception Handling)
3. **Phase 3 (Enhancement)**: Items 9-15 (Security, Monitoring, Dependencies, Performance)

## 🧪 Testing Strategy

### Unit Tests Needed:
- All service implementations
- All mapper classes
- All custom validators
- All configuration classes

### Integration Tests Needed:
- All REST controllers
- All repository classes
- Security configuration
- Database migrations

### Test Coverage Goal:
- Minimum 80% code coverage
- 100% coverage for critical business logic
- All edge cases and error scenarios covered

## 📝 Documentation Updates

- Update README.md with setup instructions
- Document API endpoints and authentication
- Create deployment guide
- Document configuration properties
- Add troubleshooting guide

---

**Estimated Timeline**: 2-3 weeks for full production readiness
**Critical Path**: Remove Lombok → Implement Flyway → Complete Testing → Production Configuration