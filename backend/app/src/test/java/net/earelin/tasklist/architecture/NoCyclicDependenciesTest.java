package net.earelin.tasklist.architecture;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * ArchUnit tests to ensure no cyclic dependencies between packages.
 */
@AnalyzeClasses(
    packages = "net.earelin.tasklist",
    importOptions = ImportOption.DoNotIncludeTests.class
)
class NoCyclicDependenciesTest {

  @ArchTest
  static final ArchRule noCyclicDependencies = slices()
      .matching("net.earelin.tasklist.(*)..")
      .should().beFreeOfCycles();
}
