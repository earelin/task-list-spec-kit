import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Home | Task List",
  description:
    "Welcome to Task List - organize your tasks and boost your productivity.",
};

/**
 * Home page component - Server-rendered (SSR) for FR-002, FR-003.
 * This is a React Server Component by default in Next.js App Router.
 */
export default function HomePage() {
  return (
    <div className="flex min-h-screen flex-col">
      {/* Header - semantic HTML for FR-014 */}
      <header
        className="border-border bg-card border-b"
        role="banner"
        aria-label="Site header"
      >
        <div className="container mx-auto flex h-16 items-center px-4">
          <h1 className="text-foreground text-xl font-semibold">Task List</h1>
        </div>
      </header>

      {/* Main content area - FR-003, FR-014 */}
      <main
        id="main-content"
        className="container mx-auto flex-1 px-4 py-8"
        role="main"
        aria-label="Main content"
      >
        <section aria-labelledby="welcome-heading">
          <h2
            id="welcome-heading"
            className="text-foreground mb-4 text-3xl font-bold tracking-tight"
          >
            Welcome to Task List
          </h2>
          <p className="text-muted-foreground mb-6 max-w-2xl text-lg">
            A modern task management application designed to help you organize
            your work efficiently. Track your tasks, set priorities, and achieve
            your goals.
          </p>

          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {/* Feature cards */}
            <article className="border-border bg-card rounded-lg border p-6 shadow-sm">
              <h3 className="text-card-foreground mb-2 text-lg font-semibold">
                Organize Tasks
              </h3>
              <p className="text-muted-foreground">
                Create, categorize, and manage your tasks with ease.
              </p>
            </article>

            <article className="border-border bg-card rounded-lg border p-6 shadow-sm">
              <h3 className="text-card-foreground mb-2 text-lg font-semibold">
                Track Progress
              </h3>
              <p className="text-muted-foreground">
                Monitor your productivity and celebrate achievements.
              </p>
            </article>

            <article className="border-border bg-card rounded-lg border p-6 shadow-sm">
              <h3 className="text-card-foreground mb-2 text-lg font-semibold">
                Stay Focused
              </h3>
              <p className="text-muted-foreground">
                Prioritize what matters most and accomplish your goals.
              </p>
            </article>
          </div>
        </section>
      </main>

      {/* Footer - semantic HTML for FR-014 */}
      <footer
        className="border-border bg-card border-t"
        role="contentinfo"
        aria-label="Site footer"
      >
        <div className="container mx-auto px-4 py-6">
          <p className="text-muted-foreground text-center text-sm">
            &copy; {new Date().getFullYear()} Task List. Built with
            accessibility in mind.
          </p>
        </div>
      </footer>
    </div>
  );
}
