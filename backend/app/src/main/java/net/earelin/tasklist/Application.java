package net.earelin.tasklist;

import io.micronaut.runtime.Micronaut;

/**
 * Main application entry point for the Task List backend service.
 */
public class Application {

  /**
   * Application entry point.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    Micronaut.run(Application.class, args);
  }
}
