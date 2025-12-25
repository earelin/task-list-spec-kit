import { cn } from "@/lib/utils";

interface HeaderProps {
  className?: string;
}

/**
 * Header component - Site header with navigation.
 * Uses semantic HTML for accessibility (FR-014).
 */
export function Header({ className }: HeaderProps) {
  return (
    <header
      className={cn("border-border bg-card border-b", className)}
      role="banner"
      aria-label="Site header"
    >
      <div className="container mx-auto flex h-16 items-center justify-between px-4">
        <div className="flex items-center gap-2">
          <h1 className="text-foreground text-xl font-semibold">Task List</h1>
        </div>

        {/* Navigation placeholder - will be populated in future features */}
        <nav aria-label="Main navigation">
          {/* Navigation items will be added here */}
        </nav>
      </div>
    </header>
  );
}
