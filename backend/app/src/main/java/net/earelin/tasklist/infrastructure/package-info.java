/**
 * Infrastructure layer containing technical implementations and adapters.
 *
 * <p>This layer provides concrete implementations of interfaces defined in the
 * domain layer and handles all external concerns. It contains:
 * <ul>
 *   <li>Repository implementations (database adapters)</li>
 *   <li>External service clients (HTTP, messaging)</li>
 *   <li>Framework-specific configurations</li>
 *   <li>Technical services (caching, monitoring, security)</li>
 * </ul>
 *
 * <p><strong>Architectural constraints:</strong>
 * <ul>
 *   <li>May depend on domain and application layers</li>
 *   <li>Implements domain interfaces (ports)</li>
 *   <li>Contains all framework-specific code and annotations</li>
 * </ul>
 *
 * @see net.earelin.tasklist.domain
 * @see net.earelin.tasklist.application
 */
package net.earelin.tasklist.infrastructure;
