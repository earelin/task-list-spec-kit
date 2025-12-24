# Tasks: Backend Scaffolding

**Input**: Design documents from `/specs/001-backend-scaffolding/`
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/

**Tests**: Tests ARE included - spec.md requires architectural fitness tests (FR-004, FR-008) and health endpoint tests.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3, US4)
- Include exact file paths in descriptions

## Path Conventions

- **Project type**: Multiproject Gradle with `app` subproject
- **Root**: `backend/` at repository root
- **Application code**: `backend/app/src/main/java/net/earelin/tasklist/`
- **Test code**: `backend/app/src/test/java/net/earelin/tasklist/`
- **Resources**: `backend/app/src/main/resources/`

## Reference Documents

| Phase | Primary Reference | Section |
|-------|-------------------|---------|
| Setup | research.md | §3 (Gradle), §1 (Micronaut) |
| Foundational | research.md | §6 (Logback), plan.md §Project Structure |
| US1 Health | contracts/health-api.yaml, data-model.md | §HealthStatus |
| US2 Architecture | research.md | §7 (ArchUnit), §Package Structure |
| US3 Quality | research.md | §8 (CheckStyle), §9 (SonarQube) |
| US4 CI | research.md | §10 (GitHub Actions), plan.md §1.1 (Dockerfile) |

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Initialize Gradle multiproject structure with Micronaut dependencies

**Reference**: research.md §1 (Micronaut 4.10.6), §3 (Gradle 8.x Kotlin DSL)

- [x] T001 Create `backend/settings.gradle.kts` with Micronaut Platform catalog plugin (io.micronaut.platform.catalog:4.6.1) and app subproject include
- [x] T002 Create `backend/build.gradle.kts` root build file with allprojects Java 21 toolchain configuration
- [x] T003 Create `backend/gradle.properties` with Micronaut version (4.10.6) and project group
- [x] T004 [P] Initialize Gradle wrapper in `backend/` directory (gradle wrapper --gradle-version 8.12)
- [x] T005 Create `backend/app/build.gradle.kts` with Micronaut application plugin, dependencies: micronaut-http-server-jetty, micronaut-management, micronaut-test-junit5, archunit-junit5:1.4.1, logstash-logback-encoder:9.0

**Checkpoint**: `./gradlew build` runs without errors (no source code yet)

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core application skeleton that MUST be complete before ANY user story can be implemented

**Reference**: plan.md §Project Structure, research.md §6 (Logback)

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T006 Create `backend/app/src/main/java/net/earelin/tasklist/Application.java` Micronaut entry point with @MicronautApplication annotation
- [x] T007 [P] Create package structure skeleton: `backend/app/src/main/java/net/earelin/tasklist/domain/` (empty .gitkeep)
- [x] T008 [P] Create package structure skeleton: `backend/app/src/main/java/net/earelin/tasklist/application/` (empty .gitkeep)
- [x] T009 [P] Create package structure skeleton: `backend/app/src/main/java/net/earelin/tasklist/infrastructure/web/` (empty .gitkeep)
- [x] T010 Create `backend/app/src/main/resources/application.yml` with server port 8080, application name, health endpoint configuration (enabled, not sensitive, details-visible: ANONYMOUS)
- [x] T011 Create `backend/app/src/main/resources/logback.xml` with LogstashEncoder for JSON structured logging to stdout per FR-010

**Checkpoint**: `./gradlew :app:run` starts Micronaut application on port 8080

---

## Phase 3: User Story 1 - Verify Backend Health (Priority: P1) 🎯 MVP

**Goal**: Operations engineers can verify the backend service is running and healthy with version and uptime information

**Reference**: contracts/health-api.yaml, data-model.md §HealthStatus, research.md §4 (micronaut-management)

**Independent Test**: `curl http://localhost:8080/health` returns JSON with status UP, version, uptime, timestamp

### Implementation for User Story 1

- [ ] T012 [US1] Create `backend/app/src/main/java/net/earelin/tasklist/infrastructure/web/HealthController.java` extending Micronaut's HealthEndpoint to add version and uptime fields per contracts/health-api.yaml response schema
- [ ] T013 [US1] Create `backend/app/src/main/java/net/earelin/tasklist/infrastructure/config/HealthInfoProvider.java` singleton bean tracking application start time for uptime calculation (ISO 8601 duration format PT#H#M#S)
- [ ] T014 [US1] Update `backend/app/src/main/resources/application.yml` to set application version from gradle.properties (micronaut.application.version)
- [ ] T015 [US1] Create `backend/app/src/test/java/net/earelin/tasklist/infrastructure/web/HealthControllerTest.java` with @MicronautTest verifying: GET /health returns 200, response contains status/version/uptime/timestamp fields, response time <100ms (SC-001)

**Checkpoint**: Health endpoint returns complete response per contracts/health-api.yaml. All acceptance scenarios for US1 pass.

---

## Phase 4: User Story 2 - Project Structure Compliance (Priority: P2)

**Goal**: Developers can navigate and extend the codebase with clear architectural layers enforced by tests

**Reference**: research.md §7 (ArchUnit 1.4.1), §Package Structure

**Independent Test**: `./gradlew :app:test --tests "*LayerDependencyTest*"` passes, verifying layer constraints

### Implementation for User Story 2

- [ ] T016 [US2] Create `backend/app/src/test/java/net/earelin/tasklist/architecture/LayerDependencyTest.java` with ArchUnit layeredArchitecture() test: Domain layer (..domain..) may not access any layer, Application layer (..application..) may only access Domain, Infrastructure layer (..infrastructure..) may access Application and Domain
- [ ] T017 [US2] Create `backend/app/src/test/java/net/earelin/tasklist/architecture/NamingConventionTest.java` with ArchUnit tests: Controllers end with "Controller", Services end with "Service", classes in domain package have no framework annotations
- [ ] T018 [US2] Create `backend/app/src/test/java/net/earelin/tasklist/architecture/NoCyclicDependenciesTest.java` with ArchUnit slices().matching("net.earelin.tasklist.(*)..").should().beFreeOfCycles()

**Checkpoint**: All ArchUnit tests pass. Layer violations cause build failure (FR-008). Acceptance scenarios for US2 pass.

---

## Phase 5: User Story 3 - Quality Tooling Integration (Priority: P3)

**Goal**: Code style is enforced automatically with violations reported in XML format for SonarQube integration

**Reference**: research.md §8 (CheckStyle 12.3.0), §9 (SonarQube 7.2.2.6593)

**Independent Test**: `./gradlew checkstyleMain checkstyleTest` produces XML reports in app/build/reports/checkstyle/

### Implementation for User Story 3

- [ ] T019 [US3] Create `backend/config/checkstyle/checkstyle.xml` with Google Java Style rules from https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml
- [ ] T020 [US3] Update `backend/build.gradle.kts` to apply checkstyle plugin with toolVersion 12.3.0, configFile pointing to config/checkstyle/checkstyle.xml, maxWarnings=0, maxErrors=0
- [ ] T021 [US3] Configure CheckStyle task in `backend/build.gradle.kts` to generate XML and HTML reports (xml.required=true, html.required=true, sarif.required=false)
- [ ] T022 [US3] Add SonarQube plugin (org.sonarqube:7.2.2.6593) to `backend/build.gradle.kts` with sonar.java.checkstyle.reportPaths property pointing to app/build/reports/checkstyle/main.xml,app/build/reports/checkstyle/test.xml
- [ ] T023 [US3] Verify all existing source files pass CheckStyle validation, fix any violations

**Checkpoint**: `./gradlew check` runs CheckStyle and produces XML reports. Style violations fail the build (FR-009). Acceptance scenarios for US3 pass.

---

## Phase 6: User Story 4 - CI Pipeline Foundation (Priority: P4)

**Goal**: Pull requests are automatically validated with build, tests, and quality checks via GitHub Actions

**Reference**: research.md §10 (GitHub Actions versions), plan.md §1.1 (Dockerfile with OpenTelemetry)

**Independent Test**: Create a PR to trigger CI pipeline, verify all steps execute and report status

### Implementation for User Story 4

- [ ] T024 [US4] Create `backend/Dockerfile` with eclipse-temurin:21-jre-alpine base, ADD OpenTelemetry Java Agent from GitHub releases, COPY app jar, ENTRYPOINT with -javaagent flag per plan.md §1.1
- [ ] T025 [US4] Create `backend/.github/workflows/ci.yml` with: trigger on pull_request to trunk, jobs for build/test/checkstyle/sonar
- [ ] T026 [US4] Configure CI workflow job: checkout (actions/checkout@v6), setup-java (actions/setup-java@v5 with java 21 temurin), gradle wrapper validation (gradle/wrapper-validation-action@v4)
- [ ] T027 [US4] Configure CI workflow build step: gradle/actions/setup-gradle@v5, run `./gradlew build check` with Gradle caching enabled
- [ ] T028 [US4] Configure CI workflow SonarQube step: run `./gradlew sonar` with SONAR_TOKEN secret, conditional on secrets.SONAR_TOKEN being available
- [ ] T029 [US4] Add Dockerfile linting step to CI using hadolint/hadolint-action@v3.1.0

**Checkpoint**: PR triggers CI pipeline. Build, tests, and quality checks run automatically. Failures are clearly reported (FR-007). Acceptance scenarios for US4 pass.

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Final validation and documentation

- [ ] T030 [P] Run full build validation: `./gradlew clean build check` passes with zero warnings
- [ ] T031 [P] Validate quickstart.md instructions work end-to-end (clone, build, run, curl health)
- [ ] T032 Verify health endpoint response time <100ms p99 under load (10 concurrent requests)
- [ ] T033 Create sample PR to verify CI pipeline executes correctly

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3-6)**: All depend on Foundational phase completion
  - US1 (Health) can proceed first - provides running application for other stories to build on
  - US2 (Architecture) can run in parallel with US1 after Foundational
  - US3 (Quality) can run in parallel with US1/US2 after Foundational
  - US4 (CI) depends on US1-US3 being complete to have meaningful content to validate
- **Polish (Phase 7)**: Depends on all user stories being complete

### User Story Dependencies

| Story | Depends On | Can Start After |
|-------|------------|-----------------|
| US1 (P1) | Foundational | Phase 2 complete |
| US2 (P2) | Foundational | Phase 2 complete (parallel with US1) |
| US3 (P3) | Foundational | Phase 2 complete (parallel with US1, US2) |
| US4 (P4) | US1, US2, US3 | Phases 3-5 complete |

### Within Each User Story

- Implementation tasks execute sequentially (numbered order)
- Test tasks (if present) execute before implementation validates them
- Story complete before moving to next priority (unless parallelizing)

### Parallel Opportunities

Within Phase 1 (Setup):
- T004 (gradle wrapper) can run in parallel after T001-T003

Within Phase 2 (Foundational):
- T007, T008, T009 (package directories) can run in parallel

Across User Stories:
- US1, US2, US3 can execute in parallel after Foundational (different files, no dependencies)

---

## Parallel Execution Examples

### Setup Phase Parallel Tasks

```bash
# After T001-T003 complete, these can run in parallel:
Task: T004 "Initialize Gradle wrapper"
Task: T005 "Create app/build.gradle.kts"  # Sequential - depends on T001-T003
```

### Foundational Phase Parallel Tasks

```bash
# After T006 (Application.java), these can run in parallel:
Task: T007 "Create domain/ package skeleton"
Task: T008 "Create application/ package skeleton"
Task: T009 "Create infrastructure/web/ package skeleton"
```

### User Story Parallel Execution (with team)

```bash
# After Foundational (Phase 2) completes:
Developer A: US1 tasks T012-T015 (Health endpoint)
Developer B: US2 tasks T016-T018 (ArchUnit tests)
Developer C: US3 tasks T019-T023 (CheckStyle)
# All can proceed independently
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup (T001-T005)
2. Complete Phase 2: Foundational (T006-T011)
3. Complete Phase 3: User Story 1 (T012-T015)
4. **STOP and VALIDATE**: `curl http://localhost:8080/health` returns complete response
5. Deploy/demo if ready - you have a running health check endpoint!

### Incremental Delivery

1. Setup + Foundational → Application starts ✓
2. Add US1 → Health endpoint works → **MVP deployed**
3. Add US2 → Architecture enforced → Quality gates active
4. Add US3 → Style checking works → Full quality pipeline
5. Add US4 → CI validates PRs → Production-ready workflow

### Full Implementation (Sequential)

Total tasks: 33
Estimated parallel opportunities: 8 tasks can run in parallel at various points

1. Phase 1: 5 tasks (Setup)
2. Phase 2: 6 tasks (Foundational)
3. Phase 3: 4 tasks (US1 - Health)
4. Phase 4: 3 tasks (US2 - Architecture)
5. Phase 5: 5 tasks (US3 - Quality)
6. Phase 6: 6 tasks (US4 - CI)
7. Phase 7: 4 tasks (Polish)

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story is independently completable and testable
- Reference documents listed at top of file for each phase
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- All file paths are relative to repository root
