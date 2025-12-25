# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Task List is a full-stack application with a Java Micronaut backend and Next.js frontend. The project uses SpecKit (in `.specify/` and `.claude/commands/`) for specification-driven development with slash commands like `/speckit.specify`, `/speckit.plan`, `/speckit.tasks`, and `/speckit.implement`.

## Commands

### Backend (in backend/)
```bash
./gradlew build                    # Build and run all checks
./gradlew test                     # Run all tests
./gradlew :app:test --tests "*.HealthControllerTest"  # Run single test class
./gradlew :app:run                 # Start dev server
```

### Frontend (in frontend/)
```bash
npm run dev                        # Start dev server (http://localhost:3000)
npm run build                      # Production build
npm run lint                       # Run ESLint
npm run lint:fix                   # Fix ESLint issues
npm run format                     # Format with Prettier
npm run format:check               # Check formatting
npm run test                       # Run Jest tests
npm run test -- utils.test.ts      # Run single test file
npm run test:watch                 # Watch mode
npm run test:e2e                   # Run Playwright E2E tests
npm run test:a11y                  # Run accessibility tests
```

## Architecture

### Backend (Hexagonal Architecture)
- `net.earelin.tasklist.domain` - Core business logic (no external dependencies)
- `net.earelin.tasklist.application` - REST controllers, DTOs
- `net.earelin.tasklist.infrastructure` - Framework implementations

Layer dependencies enforced via ArchUnit tests - domain cannot depend on application or infrastructure.

### Frontend (Next.js App Router)
- `src/app/` - Next.js pages and layouts (SSR)
- `src/components/ui/` - shadcn/ui components
- `src/components/layout/` - Shell, Header, Footer
- `src/lib/` - Utilities (cn helper for Tailwind class merging)
- `src/services/` - API clients
- `tests/unit/` - Jest tests
- `tests/e2e/` - Playwright tests

### Specifications (specs/)
Each feature has its own directory (e.g., `specs/001-backend-scaffolding/`) containing:
- `spec.md` - Feature specification
- `plan.md` - Technical implementation plan
- `tasks.md` - Task breakdown for implementation
- `research.md` - Technical decisions
- `checklists/` - Quality checklists

## Code Style

- **Java**: Google Java Format via CheckStyle (2-space indentation)
- **TypeScript**: ESLint 9.x flat config + Prettier
- **CSS**: TailwindCSS utility classes only

## Quality Gates (from Constitution)

- 80% code coverage for business logic
- WCAG 2.1 AA accessibility compliance
- Lighthouse accessibility score ≥90
- SonarQube Cloud with zero blocker/critical issues
- API response times <200ms p95

## CI/CD Pipelines

Three pipelines per constitution:
- **CI** (`*-ci.yml`): PR validation - build, lint, test, security scan
- **CD** (`*-cd.yml`): Trunk merge - deploy to development
- **Release** (`*-release.yml`): GitHub release - deploy to production

## Key Technical Decisions

- Node.js version managed via Volta (pinned in package.json)
- Frontend uses `next/jest` for TypeScript transformation (not ts-jest)
- OpenTelemetry for distributed tracing (`@vercel/otel` + explicit OTel packages)
- Tests excluded from Next.js TypeScript build (tsconfig.json excludes `tests/**/*`)
