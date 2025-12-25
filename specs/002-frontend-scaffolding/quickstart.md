# Quickstart: Frontend Scaffolding

**Date**: 2025-12-25
**Feature**: 002-frontend-scaffolding

## Prerequisites

- Volta (Node.js version manager)
- Git
- Hadolint (for Dockerfile linting)
- Checkov (for security scanning)

**Note**: Node.js 24.x and npm 10.x are pinned in `package.json` and will be automatically
installed/used by Volta when you enter the project directory.

## Setup

### 1. Navigate to frontend directory

```bash
cd frontend
```

### 2. Install dependencies

```bash
npm install
```

### 3. Initialize shadcn/ui (if not already done)

```bash
npx shadcn@latest init
```

### 4. Start development server

```bash
npm run dev
```

The application will be available at `http://localhost:3000`.

## Available Scripts

| Command | Description |
|---------|-------------|
| `npm run dev` | Start development server with hot reload |
| `npm run build` | Build production bundle |
| `npm run start` | Start production server |
| `npm run lint` | Run ESLint |
| `npm run lint:fix` | Run ESLint with auto-fix |
| `npm run format` | Run Prettier formatting |
| `npm run format:check` | Check Prettier formatting |
| `npm run test` | Run Jest unit tests |
| `npm run test:e2e` | Run Playwright E2E tests |
| `npm run test:a11y` | Run accessibility tests |

## Project Structure

```text
frontend/
├── src/
│   ├── app/                 # Next.js App Router
│   │   ├── layout.tsx       # Root layout
│   │   ├── page.tsx         # Home page
│   │   └── globals.css      # Global styles
│   ├── components/
│   │   ├── ui/              # shadcn/ui components
│   │   └── layout/          # Layout components
│   ├── lib/
│   │   └── utils.ts         # Utility functions
│   └── services/            # API clients
├── tests/
│   ├── unit/                # Unit tests
│   └── e2e/                 # E2E tests
└── public/                  # Static assets
```

## Configuration Files

| File | Purpose |
|------|---------|
| `next.config.ts` | Next.js configuration |
| `tailwind.config.ts` | TailwindCSS configuration |
| `components.json` | shadcn/ui configuration |
| `tsconfig.json` | TypeScript configuration |
| `eslint.config.mjs` | ESLint flat config |
| `prettier.config.mjs` | Prettier configuration |
| `jest.config.ts` | Jest configuration |
| `playwright.config.ts` | Playwright configuration |

## Quality Checks

### Linting

```bash
# Check for issues
npm run lint

# Auto-fix issues
npm run lint:fix

# Generate JSON report for SonarQube
npm run lint -- -f json -o eslint-results.json
```

### Formatting

```bash
# Check formatting
npm run format:check

# Apply formatting
npm run format
```

### Testing

```bash
# Unit tests
npm run test

# E2E tests (requires dev server running)
npm run test:e2e

# Accessibility tests
npm run test:a11y
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `NEXT_PUBLIC_APP_URL` | Public application URL | `http://localhost:3000` |
| `OTEL_EXPORTER_OTLP_ENDPOINT` | OpenTelemetry collector endpoint | - |
| `OTEL_SERVICE_NAME` | Service name for tracing | `task-list-frontend` |

## Docker

### Lint Dockerfile

```bash
# Hadolint - best practices validation
hadolint Dockerfile

# Checkov - security scanning
checkov -f Dockerfile
```

### Build image

```bash
docker build -t task-list-frontend .
```

### Run container

```bash
docker run -p 3000:3000 task-list-frontend
```

## Verification

After setup, verify the scaffolding works:

1. **Page loads**: Navigate to `http://localhost:3000` - should see welcome page
2. **No console errors**: Browser console should be clean
3. **Linting passes**: `npm run lint` exits with code 0
4. **Formatting passes**: `npm run format:check` exits with code 0
5. **Tests pass**: `npm run test` exits with code 0
6. **Accessibility**: Lighthouse accessibility score ≥90
7. **Dockerfile lint passes**: `hadolint Dockerfile` and `checkov -f Dockerfile` exit with code 0

## Troubleshooting

### Volta not installed

Install Volta first:

```bash
# macOS/Linux
curl https://get.volta.sh | bash

# Windows (PowerShell)
winget install Volta.Volta
```

### Node.js version mismatch

With Volta, the correct Node.js version is automatic. Verify Volta is working:

```bash
volta --version   # Should show Volta version
node --version    # Should show v24.x.x (auto-installed by Volta)
```

If Node.js version is wrong, ensure Volta is in your PATH and run:

```bash
volta install node@24
```

### Port already in use

If port 3000 is busy, use a different port:

```bash
npm run dev -- -p 3001
```

### shadcn/ui components missing

Re-run the init command:

```bash
npx shadcn@latest init
```

## Next Steps

1. Run `/speckit.tasks` to generate implementation tasks
2. Implement tasks following the defined structure
3. Ensure all quality checks pass before PR
