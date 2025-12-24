package net.earelin.tasklist.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * ArchUnit tests for naming conventions.
 * Ensures consistent naming patterns across the codebase.
 */
@AnalyzeClasses(
    packages = "net.earelin.tasklist",
    importOptions = ImportOption.DoNotIncludeTests.class
)
class NamingConventionTest {

  @ArchTest
  static final ArchRule controllersShouldEndWithController = classes()
      .that().resideInAPackage("..rest..")
      .and().areAnnotatedWith(io.micronaut.http.annotation.Controller.class)
      .should().haveSimpleNameEndingWith("Controller");

  @ArchTest
  static final ArchRule domainClassesShouldNotHaveFrameworkAnnotations = noClasses()
      .that().resideInAPackage("..domain..")
      .should().beAnnotatedWith(io.micronaut.http.annotation.Controller.class)
      .orShould().beAnnotatedWith(jakarta.inject.Singleton.class)
      .orShould().beAnnotatedWith(jakarta.inject.Inject.class);
}
