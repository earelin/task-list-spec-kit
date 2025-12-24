package net.earelin.tasklist.architecture;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * ArchUnit tests for hexagonal architecture layer dependency rules.
 * Enforces that domain layer has no external dependencies,
 * application layer only accesses domain, and infrastructure
 * can access both application and domain.
 */
@AnalyzeClasses(
    packages = "net.earelin.tasklist",
    importOptions = ImportOption.DoNotIncludeTests.class
)
class LayerDependencyTest {

  @ArchTest
  static final ArchRule layerDependenciesAreRespected = layeredArchitecture()
      .consideringOnlyDependenciesInLayers()
      .layer("Domain").definedBy("..domain..")
      .layer("Application").definedBy("..application..")
      .layer("Infrastructure").definedBy("..infrastructure..")
      .whereLayer("Domain").mayNotAccessAnyLayer()
      .whereLayer("Application").mayOnlyAccessLayers("Domain")
      .whereLayer("Infrastructure").mayOnlyAccessLayers("Application", "Domain");
}
