# GitHub Actions Workflows

This directory contains GitHub Actions workflows for automating test generation and execution for the AI Rest Assured Demo project.

## Available Workflows

### 1. Generate and Run API Tests (`generate-and-run-tests.yml`)

This workflow handles both test generation using AI and test execution:

- **Trigger**: On push to main branch, on pull requests to main branch, or manual trigger
- **Manual Trigger Options**: You can optionally provide an AI API key when manually triggering
- **Process**:
  - Builds the project
  - Configures and uses the AI service to generate test cases
  - Runs the generated tests
  - Archives test results as an artifact

### 2. Run API Tests (`run-tests.yml`)

This workflow only runs previously generated tests:

- **Trigger**: When tests are pushed to the repository, on a weekly schedule, or manual trigger
- **Process**:
  - Builds the project
  - Runs existing tests without generating new ones
  - Generates a simple test report
  - Archives test results and report as artifacts

## Secrets Configuration

To use these workflows with AI services, you need to configure the following secret in your GitHub repository:

1. `AI_API_KEY`: OpenAI API key
2. `CLAUDE_AI_API_KEY`: Claude API key 

To add this secret:
1. Go to your repository on GitHub
2. Click on "Settings" > "Secrets and variables" > "Actions"
3. Click "New repository secret"
4. Add the names `AI_API_KEY`, `CLAUDE_AI_API_KEY` and your actual API key as the value

## Using the Workflows

### To Generate and Run Tests

You can manually trigger the "Generate and Run API Tests" workflow:
1. Go to the "Actions" tab in your repository
2. Select "Generate and Run API Tests" workflow
3. Click "Run workflow"
4. Optionally provide an API key (if you don't want to use the repository secret)
5. Click "Run workflow" to start the process

### To Only Run Existing Tests

The "Run API Tests" workflow will automatically run when tests are pushed to the repository or on the weekly schedule. You can also manually trigger it following the same steps as above.
