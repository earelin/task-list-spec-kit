import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // Generate source maps for production debugging
  productionBrowserSourceMaps: true,

  // Strict mode for React
  reactStrictMode: true,

  // Disable x-powered-by header for security
  poweredByHeader: false,

  // Enable standalone output for Docker deployment
  output: "standalone",
};

export default nextConfig;
