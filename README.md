# QualityGate Research Project

Standalone Java project for research on AI-generated unit testing. Focuses on Service Layer testing.

## Architecture

### Domain Layer (`com.qualitygate.research.domain`)
- `User` - User entity
- `Order` - Order entity
- `OrderItem` - Order item entity

### Service Layer (`com.qualitygate.research.service`)
Core business logic under test:

**OrderService** (Complex Service)
- `calculateOrderTotal(Order)` - Main pricing calculation
- `validateOrder(Order)` - Order validation

**UserService** (Simple Service)
- `isValidEmail(String)`
- `isValidUsername(String)`
- `createUser(String, String)`
- `updateUserEmail(User, String)`
- `activateUser(User)` / `deactivateUser(User)`

### Configuration Layer (`com.qualitygate.research.config`)
- `DiscountConfiguration` - Discount rules and thresholds

## Project Structure

```
QualityGate-AI-Research/
├── pom.xml
├── src/
│   ├── main/java/com/qualitygate/research/
│   │   ├── domain/
│   │   ├── service/
│   │   └── config/
│   └── test/java/com/qualitygate/research/service/
```

## Building

```bash
mvn clean compile
mvn test
mvn jacoco:report
mvn org.pitest:pitest-maven:mutationCoverage
```

## Using with QualityGate-AI Tool

Generate tests from QualityGate-AI project:

```bash
cd /path/to/QualityGate-AI
source venv/bin/activate

python3 -m src.cli generate \
  --module /absolute/path/to/QualityGate-AI-Research/src/main/java/com/qualitygate/research/service/UserService.java \
  --output /absolute/path/to/QualityGate-AI-Research/src/test/java/com/qualitygate/research/service \
  --protocol expert
```

Then run tests:

```bash
cd /path/to/QualityGate-AI-Research
mvn clean test
```

## Dependencies

- Java 11+
- Maven 3.6+
- JUnit 5
- Mockito
- JaCoCo
- PIT

## CI/CD

See `.github/workflows/ci.yml` for automated testing and reporting.
