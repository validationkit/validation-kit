# Contributing to Validation Kit

Thank you for your interest in contributing to **Validation Kit**! We welcome contributions from the community to help make this library better for everyone.

## Table of Contents
- [Prerequisites](#prerequisites)
- [How to Contribute](#how-to-contribute)
- [Development Workflow](#development-workflow)
- [Code Style](#code-style)
- [Getting Help](#getting-help)

## Prerequisites

Before you begin, ensure you have the following installed:
- **Java 17** or higher
- **Maven 3.8+**
- An IDE (IntelliJ IDEA, Eclipse, VS Code)

## How to Contribute

### Reporting Bugs
If you find a bug, please create a GitHub issue with:
1.  A descriptive title.
2.  Steps to reproduce the issue.
3.  Expected vs. actual behavior.
4.  Environment details (Java version, Spring Boot version).

### Suggesting Enhancements
We love new ideas! If you have a feature request (e.g., a new validator), please open an issue to discuss it before starting implementation.

### Submitting Pull Requests
1.  **Fork** the repository.
2.  **Clone** your fork locally.
3.  Create a new **branch** for your feature or fix:
    ```bash
    git checkout -b feature/my-awesome-validator
    ```
4.  **Commit** your changes with clear messages.
5.  **Push** to your fork and submit a **Pull Request** (PR).

## Development Workflow

1.  **Build the project:**
    ```bash
    mvn clean install
    ```
2.  **Run tests:**
    ```bash
    mvn test
    ```
    Ensure all tests pass before submitting your PR.

## Code Style

- Follow standard Java coding conventions.
- Keep classes and methods small and focused.
- write meaningful Javadoc for public classes and methods.
- Add unit tests for any new logic.

## Getting Help

If you have questions, feel free to start a discussion in the GitHub Issues section.

Happy Coding! 🚀
