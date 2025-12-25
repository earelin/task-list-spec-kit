# Research: Frontend Scaffolding

**Date**: 2025-12-25
**Feature**: 002-frontend-scaffolding

## Decisions

### SSR Framework

**Decision**: Next.js 16.x (App Router)

**Rationale**:
- 65% market share among full-stack JS frameworks, largest ecosystem
- Official OpenTelemetry support via `@vercel/otel` aligns with backend tracing strategy
- Built-in Core Web Vitals reporting via `useReportWebVitals` hook
- Proven patterns for ESLint/Prettier/Jest/Playwright/GitHub Actions
- Enterprise-grade stability and extensive documentation
- Version 16 includes latest React 19 features and performance improvements

**Alternatives Considered**:
- Next.js 15.x: Stable but lacks latest React 19 optimizations
- SvelteKit: Best performance and OpenTelemetry integration, but smaller ecosystem
- Nuxt: Gentlest learning curve, but no built-in OpenTelemetry support

### Node.js Version

**Decision**: Node.js 24 (managed via Volta)

**Rationale**:
- Latest stable version with newest ECMAScript features
- Best performance improvements for SSR workloads
- Long-term support planned

**Alternatives Considered**:
- Node.js 20 LTS: More conservative, widely deployed
- Node.js 22: Active LTS, but 24 preferred per user requirement

### Node.js Version Management

**Decision**: Volta

**Rationale**:
- Fast, reliable JavaScript tool manager written in Rust
- Pins Node.js and npm versions in package.json for reproducible builds
- Automatic version switching per project
- Cross-platform support (macOS, Linux, Windows)
- Works seamlessly with CI/CD (respects package.json pinning)

**Alternatives Considered**:
- nvm: Shell-based, slower, no per-project automatic switching
- fnm: Good alternative, but Volta has better Windows support
- asdf: Multi-language but more complex setup

### Component Library

**Decision**: shadcn/ui + TailwindCSS

**Rationale**:
- shadcn/ui provides accessible, customizable components (not a dependency - copied into project)
- Built on Radix UI primitives with WCAG 2.1 AA accessibility
- TailwindCSS offers utility-first styling with excellent IDE support
- Full control over component source code for customization
- Consistent design system foundation

**Alternatives Considered**:
- Material UI: Heavier bundle, less customizable
- Chakra UI: Good accessibility, but different design philosophy
- Headless UI: Lower-level than shadcn/ui

### Package Manager

**Decision**: npm

**Rationale**:
- Standard Node.js package manager
- Specified in clarifications
- Wide CI/CD support

**Alternatives Considered**:
- pnpm: Faster, disk-efficient (not selected)
- yarn: Workspaces support (not selected)

### Testing Framework

**Decision**: Jest + Playwright + axe-core

**Rationale**:
- Jest: Industry-standard testing framework, excellent TypeScript support, official Next.js integration
- Playwright: Cross-browser E2E testing, official Next.js support
- axe-core: Industry-standard accessibility testing library

**Alternatives Considered**:
- Vitest: Faster but less established ecosystem
- Cypress: Good E2E but heavier than Playwright

### Observability Stack

**Decision**: OpenTelemetry with `@vercel/otel`

**Rationale**:
- Official Next.js integration for distributed tracing
- Aligns with backend OpenTelemetry Java Agent
- Core Web Vitals via `useReportWebVitals`
- No browser console logs (production builds strip console)

**Alternatives Considered**:
- Custom instrumentation: More effort, less standardized
- Third-party APM: Vendor lock-in concerns

### Quality Tooling

**Decision**: ESLint 9.x (flat config) + Prettier + eslint-plugin-import

**Rationale**:
- ESLint flat config is the modern standard
- Prettier for consistent formatting
- eslint-plugin-import for architectural fitness functions (layer dependencies)
- JSON output for SonarQube integration

**Alternatives Considered**:
- Biome: Promising but less mature ecosystem

### Dockerfile Linting

**Decision**: Checkov + Hadolint

**Rationale**:
- Checkov: Security-focused linting, detects misconfigurations and vulnerabilities
- Hadolint: Dockerfile best practices, validates against Docker official guidelines
- Complementary tools - Checkov for security, Hadolint for syntax and best practices
- Both integrate well with GitHub Actions CI

**Alternatives Considered**:
- Checkov only: Misses Dockerfile-specific best practices
- Hadolint only: Misses security vulnerabilities
- Trivy: Good security scanner but less focused on Dockerfile syntax

## Technical Stack Summary

| Component | Choice | Version |
|-----------|--------|---------|
| Runtime | Node.js | 24.x |
| Framework | Next.js (App Router) | 16.x |
| Language | TypeScript | 5.x |
| Styling | TailwindCSS | 4.x |
| Components | shadcn/ui | latest |
| Linting | ESLint (flat config) | 9.x |
| Formatting | Prettier | 3.x |
| Unit Testing | Jest | 29.x |
| E2E Testing | Playwright | 1.x |
| Accessibility | axe-core | 4.x |
| Tracing | @vercel/otel | latest |
| Package Manager | npm | 10.x |
| Version Manager | Volta | latest |
| Dockerfile Linting | Checkov + Hadolint | latest |

## Key Integration Points

### OpenTelemetry Setup
- Use `instrumentation.ts` file for Next.js tracing
- Configure `@vercel/otel` for distributed tracing
- Export traces to backend collector (configured via environment)

### ESLint Report Generation
- ESLint JSON format for SonarQube: `eslint -f json -o eslint-results.json`
- SonarQube reads ESLint JSON reports directly via `sonar.eslint.reportPaths`
- For GitHub Code Scanning (optional): SARIF via `@microsoft/eslint-formatter-sarif`

### Core Web Vitals
- Use Next.js `useReportWebVitals` hook in root layout
- Report to analytics endpoint (configurable)

### Accessibility Testing
- axe-core integration in Playwright tests
- Lighthouse CI in GitHub Actions for accessibility score

### Dockerfile Linting
- Hadolint: `hadolint Dockerfile` for best practices validation
- Checkov: `checkov -f Dockerfile` for security scanning
- Run both in CI pipeline before building Docker image
- Hadolint rules: DL3008 (pin versions), DL3018 (avoid apk cache), etc.
- Checkov checks: CKV_DOCKER_2 (healthcheck), CKV_DOCKER_3 (user), etc.

## Project Structure Alignment

```text
frontend/
├── src/
│   ├── app/                 # Next.js App Router pages
│   │   ├── layout.tsx       # Root layout with providers
│   │   ├── page.tsx         # Home page
│   │   └── globals.css      # TailwindCSS imports
│   ├── components/
│   │   ├── ui/              # shadcn/ui components
│   │   └── layout/          # Layout components
│   ├── lib/
│   │   └── utils.ts         # Utility functions (cn helper)
│   ├── services/            # API clients, utilities
│   └── instrumentation.ts   # OpenTelemetry setup (Next.js convention)
├── public/                  # Static assets
├── tests/
│   ├── unit/                # Jest tests
│   └── e2e/                 # Playwright tests
├── package.json
├── next.config.ts           # Next.js configuration
├── tailwind.config.ts       # TailwindCSS configuration
├── components.json          # shadcn/ui configuration
├── tsconfig.json
├── eslint.config.mjs        # ESLint flat config
├── prettier.config.mjs
├── jest.config.ts
├── playwright.config.ts
└── Dockerfile
```
