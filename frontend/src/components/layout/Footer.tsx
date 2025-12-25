import { cn } from "@/lib/utils";

interface FooterProps {
  className?: string;
}

/**
 * Footer component - Site footer with copyright and links.
 * Uses semantic HTML for accessibility (FR-014).
 */
export function Footer({ className }: FooterProps) {
  const currentYear = new Date().getFullYear();

  return (
    <footer
      className={cn("border-border bg-card border-t", className)}
      role="contentinfo"
      aria-label="Site footer"
    >
      <div className="container mx-auto px-4 py-6">
        <div className="flex flex-col items-center justify-between gap-4 md:flex-row">
          <p className="text-muted-foreground text-sm">
            &copy; {currentYear} Task List. Built with accessibility in mind.
          </p>

          {/* Footer links placeholder - will be populated in future features */}
          <nav aria-label="Footer navigation">
            {/* Footer links will be added here */}
          </nav>
        </div>
      </div>
    </footer>
  );
}
