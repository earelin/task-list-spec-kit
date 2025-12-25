/** @type {import("prettier").Config} */
const config = {
  // Basic formatting
  semi: true,
  singleQuote: false,
  tabWidth: 2,
  useTabs: false,
  trailingComma: "es5",
  printWidth: 80,

  // JSX formatting
  jsxSingleQuote: false,
  bracketSpacing: true,
  bracketSameLine: false,

  // Arrow function parentheses
  arrowParens: "always",

  // End of line
  endOfLine: "lf",

  // Plugins
  plugins: ["prettier-plugin-tailwindcss"],

  // Tailwind plugin configuration
  tailwindFunctions: ["cn", "clsx", "twMerge"],
};

export default config;
