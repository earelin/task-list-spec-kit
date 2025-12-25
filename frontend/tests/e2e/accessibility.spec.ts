import AxeBuilder from "@axe-core/playwright";
import { test, expect } from "@playwright/test";

/**
 * Accessibility E2E tests using axe-core.
 * Validates WCAG 2.1 AA compliance (SC-005, FR-012).
 */

test.describe("Accessibility", () => {
  test("home page should have no critical accessibility violations", async ({
    page,
  }) => {
    await page.goto("/");

    // Wait for the page to be fully loaded
    await page.waitForLoadState("networkidle");

    // Run axe accessibility scan
    const accessibilityScanResults = await new AxeBuilder({ page })
      .withTags(["wcag2a", "wcag2aa", "wcag21a", "wcag21aa"])
      .analyze();

    // Filter for critical and serious violations only
    const criticalViolations = accessibilityScanResults.violations.filter(
      (violation) =>
        violation.impact === "critical" || violation.impact === "serious"
    );

    // Log violations for debugging (if any)
    if (criticalViolations.length > 0) {
      console.log("Critical accessibility violations found:");
      criticalViolations.forEach((violation) => {
        console.log(`- ${violation.id}: ${violation.description}`);
        console.log(`  Impact: ${violation.impact}`);
        console.log(`  Help: ${violation.helpUrl}`);
        violation.nodes.forEach((node) => {
          console.log(`  Element: ${node.html}`);
        });
      });
    }

    // Fail test if critical or serious violations found
    expect(criticalViolations).toHaveLength(0);
  });

  test("skip to content link should be functional", async ({ page }) => {
    await page.goto("/");

    // Focus on the skip link (it's visually hidden but should be focusable)
    await page.keyboard.press("Tab");

    // Check that skip link is visible when focused
    const skipLink = page.locator('a[href="#main-content"]');
    await expect(skipLink).toBeFocused();

    // Activate the skip link
    await page.keyboard.press("Enter");

    // Verify main content is focused or in view
    const mainContent = page.locator("#main-content");
    await expect(mainContent).toBeInViewport();
  });

  test("all interactive elements should be keyboard accessible", async ({
    page,
  }) => {
    await page.goto("/");

    // Get all focusable elements
    const focusableElements = page.locator(
      'a[href], button, input, select, textarea, [tabindex]:not([tabindex="-1"])'
    );

    const count = await focusableElements.count();

    // Tab through all focusable elements
    for (let i = 0; i < count; i++) {
      await page.keyboard.press("Tab");

      // Get the currently focused element
      const focusedElement = await page.evaluate(() => {
        const el = document.activeElement;
        return el ? el.tagName.toLowerCase() : null;
      });

      // Verify something is focused (not body)
      expect(focusedElement).not.toBe("body");
    }
  });

  test("page should have proper heading hierarchy", async ({ page }) => {
    await page.goto("/");

    // Get all headings
    const headings = await page.locator("h1, h2, h3, h4, h5, h6").all();

    // Should have exactly one h1
    const h1Count = await page.locator("h1").count();
    expect(h1Count).toBe(1);

    // Verify heading order - no skipped levels
    let previousLevel = 0;
    for (const heading of headings) {
      const tagName = await heading.evaluate((el) => el.tagName.toLowerCase());
      const currentLevel = parseInt(tagName.charAt(1));

      // Heading level should not skip more than one level
      expect(currentLevel - previousLevel).toBeLessThanOrEqual(1);
      previousLevel = currentLevel;
    }
  });

  test("images should have alt text", async ({ page }) => {
    await page.goto("/");

    const images = await page.locator("img").all();

    for (const img of images) {
      const alt = await img.getAttribute("alt");
      // All images should have alt attribute (even if empty for decorative)
      expect(alt).not.toBeNull();
    }
  });

  test("page should have proper lang attribute", async ({ page }) => {
    await page.goto("/");

    const lang = await page.locator("html").getAttribute("lang");
    expect(lang).toBe("en");
  });

  test("page should have proper page title", async ({ page }) => {
    await page.goto("/");

    const title = await page.title();
    expect(title).toBeTruthy();
    expect(title.length).toBeGreaterThan(0);
  });

  test("color contrast should meet WCAG AA standards", async ({ page }) => {
    await page.goto("/");

    // Run axe specifically for color contrast
    const accessibilityScanResults = await new AxeBuilder({ page })
      .withRules(["color-contrast"])
      .analyze();

    const contrastViolations = accessibilityScanResults.violations.filter(
      (v) => v.id === "color-contrast"
    );

    expect(contrastViolations).toHaveLength(0);
  });
});
