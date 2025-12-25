import { cn } from "@/lib/utils";

import { Footer } from "./Footer";
import { Header } from "./Header";

interface ShellProps {
  children: React.ReactNode;
  className?: string;
}

/**
 * Shell component - Application shell that composes Header and Footer.
 * Provides consistent layout structure across all pages.
 */
export function Shell({ children, className }: ShellProps) {
  return (
    <div className={cn("flex min-h-screen flex-col", className)}>
      <Header />

      <main
        id="main-content"
        className="container mx-auto flex-1 px-4 py-8"
        role="main"
        aria-label="Main content"
      >
        {children}
      </main>

      <Footer />
    </div>
  );
}
