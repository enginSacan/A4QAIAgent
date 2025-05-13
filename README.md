# AI Agent Rest Assured Demo

A demonstration project that uses AI to automatically generate and run API tests for a Swagger-defined REST API.

## Project Overview

This project demonstrates how to use AI agents (Claude or ChatGPT) to analyze a Swagger API specification, generate test cases, and implement those test cases using Rest Assured in a fully automated way.

The project:
1. Parses a Swagger/OpenAPI documentation
2. Extracts API endpoints and their specifications
3. Uses AI to generate comprehensive test cases
4. Transforms those test cases into executable Rest Assured tests
5. Runs the tests and reports results

## Getting Started

### Prerequisites

- Java 17 or later
- Maven 3.6 or later
- API key for either Claude (Anthropic) or ChatGPT (OpenAI)

### Configuration

Edit the `src/main/resources/application.properties` file to configure:

- `swagger.url` - URL of the Swagger/OpenAPI documentation
- `base.api.url` - Base URL for the API being tested
- `ai.provider` - Choose between `claude` or `openai`
- `claude.api.key` - Your Claude API key (if using Claude)
- `openai.api.key` - Your OpenAI API key (if using ChatGPT)
- Various execution options and limits

### Running the Project

To build and run the project:

```bash
mvn clean compile
mvn exec:java
```

This will:
1. Parse the Swagger documentation
2. Generate test cases using the configured AI service
3. Create Java test files
4. Run the tests
5. Report the results

### Execution Options

You can customize the execution with these properties:

- `generate.tests.only=true` - Only generate tests without running them
- `run.tests.only=true` - Only run existing tests without generating new ones
- `max.endpoints=10` - Limit the number of endpoints to process
- `max.test.cases.per.endpoint=3` - Limit the number of test cases per endpoint

## Project Structure

- `src/main/java/com/aiagent/`
  - `config/` - Application configuration
  - `model/` - Data models for API endpoints, test cases, and results
  - `service/` - Services for Swagger parsing and AI integration
  - `util/` - Utility classes for test generation
  - `Main.java` - Main application entry point
- `src/test/java/com/aiagent/generated/` - Location of generated test files

## Key Components

1. **SwaggerParserService** - Parses Swagger documentation and extracts API endpoints
2. **AiService** - Interface for AI services with implementations for Claude and OpenAI
3. **TestCaseGenerator** - Generates test cases and Rest Assured code
4. **AppConfig** - Application configuration loaded from properties

## AI-Generated Test Structure

Each generated test:
- Includes proper JUnit 5 annotations
- Uses Rest Assured for API testing
- Contains appropriate assertions and validations
- Is organized into logical test methods

## Contributing

Feel free to contribute to this project by:
1. Adding support for additional AI providers
2. Improving test generation templates
3. Enhancing the Swagger parsing capabilities
4. Adding more comprehensive test result reporting
