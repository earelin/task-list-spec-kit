"use client";

import { useReportWebVitals } from "next/web-vitals";

/**
 * Web Vitals Reporter Component.
 * Captures Core Web Vitals metrics and reports them to analytics endpoint.
 *
 * Implements FR-016: System MUST capture and expose Core Web Vitals metrics (LCP, FID, CLS).
 *
 * Metrics captured:
 * - LCP (Largest Contentful Paint): Loading performance
 * - FID (First Input Delay): Interactivity
 * - CLS (Cumulative Layout Shift): Visual stability
 * - FCP (First Contentful Paint): Initial render
 * - TTFB (Time to First Byte): Server response time
 * - INP (Interaction to Next Paint): Overall responsiveness
 */
export function WebVitals() {
  useReportWebVitals((metric) => {
    // In production, send to analytics endpoint
    // FR-018: No console.log in production - use proper reporting
    if (process.env.NODE_ENV === "development") {
      // Development-only logging for debugging
      // This will be stripped in production builds
      return;
    }

    // Send to analytics endpoint (configure based on your setup)
    const body = JSON.stringify({
      name: metric.name,
      value: metric.value,
      rating: metric.rating,
      delta: metric.delta,
      id: metric.id,
      navigationType: metric.navigationType,
    });

    // Use sendBeacon for reliability during page unload
    if (navigator.sendBeacon) {
      const analyticsEndpoint =
        process.env.NEXT_PUBLIC_ANALYTICS_ENDPOINT || "/api/vitals";
      navigator.sendBeacon(analyticsEndpoint, body);
    }
  });

  // This component doesn't render anything
  return null;
}
