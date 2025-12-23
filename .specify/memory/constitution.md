<!--
Sync Impact Report:
Version change: 1.1.0 → 2.0.0
Modified principles: III. Progressive Enhancement (UI/browser focus → modularization/evolutionary architecture focus)
Added sections: None
Removed sections: None
Templates requiring updates: ✅ No template dependencies found
Follow-up TODOs: None
-->

# Task List Spec Kit Constitution

## Core Principles

### I. Domain-Driven Design
All software architecture MUST follow Domain-Driven Design principles. Business logic MUST be encapsulated in domain entities and services that reflect real-world concepts. Technical concerns MUST be separated from business logic through clear boundaries and interfaces.

*Rationale: Ensures maintainable, testable code that aligns with business requirements and facilitates long-term evolution.*

### II. Security by Design
Security MUST be integrated from the earliest design phases. All authentication MUST use OpenID Connect. All inputs MUST be validated and sanitized. Secrets MUST never be logged or committed to version control. Security reviews MUST be conducted for all user-facing features and data access patterns.

*Rationale: Prevents security vulnerabilities through proactive design rather than reactive patching.*

### III. Progressive Enhancement
Progressive Enhancement means that we are going to take a progressive approach in the implementation of features using modularization and evolutionary architecture to progressively grow the system.

*Rationale: Ensures the system remains maintainable and scalable by building features incrementally, allowing for continuous improvement while maintaining stability through modular design and evolutionary architecture patterns.*

### IV. Test-First Development (NON-NEGOTIABLE)
All features MUST be developed using Test-Driven Development. Acceptance tests using Cucumber MUST be written before implementation. Unit tests MUST achieve minimum 90% code coverage. Integration tests MUST be maintained in separate source sets to ensure clear separation of concerns.

*Rationale: Guarantees code quality, prevents regressions, and serves as living documentation of system behavior.*

### V. Code Quality Standards
All Java code MUST follow Google Java Format. All TypeScript/JavaScript code MUST follow Prettier formatting. Code MUST pass static analysis without warnings. Cyclomatic complexity MUST not exceed 10 per method. All public APIs MUST have comprehensive documentation.

*Rationale: Maintains consistent, readable codebase that facilitates collaboration and reduces maintenance overhead.*

### VI. Performance by Design
Backend services MUST respond within 200ms for 95% of requests under normal load. Frontend pages MUST achieve Core Web Vitals "Good" ratings. Database queries MUST be optimized and monitored through Micrometer metrics. Memory usage MUST not exceed defined limits in production environments.

*Rationale: Ensures excellent user experience and system reliability under production loads.*

### VII. Observability First
All services MUST implement comprehensive logging, metrics, and tracing using Micrometer. Error states MUST be logged with sufficient context for debugging. Business metrics MUST be collected to support data-driven decisions. Health checks MUST be implemented for all service dependencies.

*Rationale: Enables rapid debugging, performance optimization, and informed operational decisions.*

## Technical Standards

### Backend Architecture
- **Language**: Java 21 with modern language features
- **Framework**: Micrometer with observability-first architecture
- **Database**: MongoDB with proper indexing strategies
- **Authentication**: OpenID Connect integration
- **APIs**: RESTful design following OpenAPI 3.0 specification
- **Build Tool**: Gradle with dependency management
- **Observability**: Micrometer for comprehensive metrics, logging, and tracing
- **Testing**: Cucumber for acceptance tests, JUnit 5 for unit tests

### Frontend Architecture
- **Framework**: Next.js 16 with TypeScript
- **Styling**: TailwindCSS for consistent design system
- **Testing**: Playwright integrated with Cucumber for acceptance testing
- **Build Tool**: npm with package-lock.json for dependency consistency
- **Code Quality**: ESLint and Prettier for code standards

### Quality Gates
- All code MUST pass automated formatting checks
- Test coverage MUST meet minimum thresholds before deployment
- Security scans MUST pass without high-severity findings
- Performance benchmarks MUST be met for critical user journeys

## Development Workflow

### Code Review Process
- All changes MUST go through peer review process
- Reviews MUST verify constitutional compliance
- Security-sensitive changes MUST include security team review
- Breaking changes MUST include migration documentation

### Testing Strategy
- Integration tests MUST be maintained in separate Gradle source sets
- Acceptance tests MUST use Cucumber with Playwright for frontend
- Performance tests MUST be included for critical paths
- Database migrations MUST be tested in isolation

### Deployment Standards
- All deployments MUST pass automated quality gates
- Production deployments MUST include rollback procedures
- Configuration MUST be externalized from application code
- Environment-specific settings MUST be validated before deployment

### Documentation Requirements
- API changes MUST include updated OpenAPI specifications
- Architecture decisions MUST be documented with rationale
- User-facing changes MUST include updated documentation
- Security procedures MUST be kept current with implementation

## Governance

### Constitutional Authority
This constitution supersedes all other development practices and guidelines. All architectural decisions MUST align with these principles. Deviations require explicit justification and approval through the amendment process.

### Amendment Process
- Constitutional changes MUST be proposed through documented RFC process
- Technical impact assessment MUST be completed for all amendments
- Implementation migration plan MUST be provided for breaking changes
- Approval requires consensus from technical leadership team

### Compliance Verification
- All pull requests MUST verify constitutional compliance
- Code reviews MUST include principle adherence checks
- Automated tooling MUST enforce formatting and quality standards
- Regular architecture reviews MUST assess overall system compliance

### Technical Decision Authority
- Design decisions prioritize constitution principles over convenience
- Performance requirements take precedence over developer preferences
- Security considerations override feature delivery timelines
- Code quality standards are non-negotiable regardless of deadlines

**Version**: 2.0.0 | **Ratified**: 2025-12-23 | **Last Amended**: 2025-12-23