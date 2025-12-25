# Implementation Plan: Frontend Scaffolding

**Branch**: `002-frontend-scaffolding` | **Date**: 2025-12-25 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/002-frontend-scaffolding/spec.md`

## Summary

Create the frontend scaffolding for the Task List application using Server-Side Rendering (SSR)
with TypeScript. The scaffolding includes project structure, quality tooling (ESLint, Prettier),
accessibility foundations (WCAG 2.1 AA), observability integration (Core Web Vitals, distributed
tracing), and CI pipeline configuration for GitHub Actions.

## Technical Context

**Language/Version**: TypeScript 5.x + Node.js 24.x (managed via Volta)
**Primary Dependencies**: Next.js 16.1.x (App Router), TailwindCSS 4.x, shadcn/ui
**Storage**: N/A (scaffolding only - no persistence layer)
**Testing**: Jest for unit tests, Playwright for E2E, axe-core for accessibility
**Target Platform**: Web browsers (Chrome, Firefox, Safari, Edge - 2 latest major versions)
**Project Type**: web (frontend component of web application)
**Performance Goals**: Initial page load <3s, Lighthouse accessibility score ≥90
**Constraints**: SSR required, WCAG 2.1 AA compliance
**Scale/Scope**: Single-page application shell with placeholder content
**Observability**: @vercel/otel for distributed tracing, useReportWebVitals for Core Web Vitals

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Requirement | Status | Notes |
|-----------|-------------|--------|-------|
| Quality First | ESLint + Prettier with JSON output | ✅ Planned | FR-006, FR-007, FR-008 |
| Quality First | 80% code coverage for business logic | ✅ Planned | Jest configured |
| Quality First | SonarQube Cloud integration | ✅ Planned | SARIF reports |
| Quality First | WCAG 2.1 AA accessibility | ✅ Planned | FR-012, SC-005, SC-007 |
| DevOps | GitHub Actions CI pipeline | ✅ Planned | FR-009 |
| DevOps | Three pipelines (CI/CD/Release) | ✅ Planned | CI in scaffolding |
| SRE | Core Web Vitals metrics | ✅ Planned | FR-016 |
| SRE | Distributed tracing | ✅ Planned | FR-017 |
| Evolutionary Architecture | Architectural fitness functions | ✅ Planned | eslint-plugin-import |
| Evolutionary Architecture | Layer dependency validation | ✅ Planned | FR-005 |
| Clean Code | ESLint for TypeScript | ✅ Planned | Constitution requirement |
| Clean Code | Prettier formatting | ✅ Planned | Constitution requirement |
| Infrastructure as Code | Dockerfile with Checkov + Hadolint | ✅ Planned | Constitution + best practice |
| Continuous Delivery | Semantic versioning | ✅ Planned | package.json version |
| Documentation | Mermaid for diagrams | ✅ Planned | Constitution v1.1.0 |

**Gate Status**: ✅ PASS - All constitutional requirements addressed in plan

## Project Structure

### Documentation (this feature)

```text
specs/002-frontend-scaffolding/
├── plan.md              # This file
├── research.md          # Phase 0 output - SSR framework decision
├── data-model.md        # Phase 1 output - minimal (scaffolding)
├── quickstart.md        # Phase 1 output - setup instructions
├── contracts/           # Phase 1 output - N/A for scaffolding
└── tasks.md             # Phase 2 output (/speckit.tasks command)
```

### Source Code (repository root)

```text
frontend/
├── src/
│   ├── app/                 # Next.js App Router pages
│   │   ├── layout.tsx       # Root layout with providers
│   │   ├── page.tsx         # Home page
│   │   └── globals.css      # TailwindCSS imports
│   ├── components/
│   │   ├── ui/              # shadcn/ui components
│   │   └── layout/          # Layout components (Header, Footer, Shell)
│   ├── lib/
│   │   └── utils.ts         # Utility functions (cn helper)
│   ├── services/            # API clients, utilities
│   └── instrumentation.ts   # OpenTelemetry setup (Next.js convention)
├── public/                  # Static assets
├── tests/
│   ├── unit/                # Jest unit tests
│   └── e2e/                 # Playwright E2E tests
├── package.json             # npm configuration
├── next.config.ts           # Next.js configuration
├── tailwind.config.ts       # TailwindCSS configuration
├── components.json          # shadcn/ui configuration
├── tsconfig.json            # TypeScript configuration
├── eslint.config.mjs        # ESLint flat config
├── prettier.config.mjs      # Prettier configuration
├── jest.config.ts           # Jest configuration
├── playwright.config.ts     # Playwright configuration
└── Dockerfile               # Container image definition
```

**Structure Decision**: Web application structure with frontend/ directory at repository root.
This aligns with the existing backend/ structure from 001-backend-scaffolding and supports
future monorepo organization.

## Complexity Tracking

> No constitutional violations requiring justification.

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| N/A | N/A | N/A |
