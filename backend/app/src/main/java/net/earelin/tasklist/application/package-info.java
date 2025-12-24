/**
 * Application layer containing use cases and API adapters.
 *
 * <p>This layer orchestrates the flow of data and coordinates domain objects
 * to perform application-specific tasks. It contains:
 * <ul>
 *   <li>REST controllers exposing HTTP endpoints</li>
 *   <li>Request/response DTOs for API contracts</li>
 *   <li>Use case implementations (application services)</li>
 *   <li>Input validation and transformation</li>
 * </ul>
 *
 * <p><strong>Architectural constraints:</strong>
 * <ul>
 *   <li>May only depend on the domain layer</li>
 *   <li>Must not contain business logic (delegate to domain)</li>
 *   <li>Must not depend on infrastructure layer</li>
 * </ul>
 *
 * @see net.earelin.tasklist.domain
 * @see net.earelin.tasklist.infrastructure
 */
package net.earelin.tasklist.application;
