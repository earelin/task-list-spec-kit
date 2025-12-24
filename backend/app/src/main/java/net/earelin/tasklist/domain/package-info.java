/**
 * Domain layer containing core business logic and entities.
 *
 * <p>This layer is the heart of the application and contains:
 * <ul>
 *   <li>Business entities and value objects</li>
 *   <li>Domain services implementing business rules</li>
 *   <li>Repository interfaces (ports) for data access</li>
 *   <li>Domain events</li>
 * </ul>
 *
 * <p><strong>Architectural constraints:</strong>
 * <ul>
 *   <li>Must not depend on any other layer (application, infrastructure)</li>
 *   <li>Must not use framework-specific annotations</li>
 *   <li>Must be purely Java with no external dependencies except standard library</li>
 * </ul>
 *
 * @see net.earelin.tasklist.application
 * @see net.earelin.tasklist.infrastructure
 */
package net.earelin.tasklist.domain;
