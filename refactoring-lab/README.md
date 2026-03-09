Legacy Java SE 7 Refactoring Lab
============================

Purpose
-------
This is a deliberately "legacy-style" Java SE 7 codebase that participants can refactor using Java 8 → Java 25 features.

Capabilities included
---------------------
- CSV ingestion + basic validation
- Reflection-based mapping (intentionally brittle)
- Concurrency with ExecutorService + Futures (no CompletableFuture)
- XML reporting with DOM
- Repository abstraction + JDBC sample (resource handling intentionally verbose)
- CLI app to run an end-to-end flow

Build & Run (baseline)
----------------------
1) Build all modules:
   mvn -q test

2) Run CLI:
   mvn -q -pl legacy-cli package
   java -jar legacy-cli/target/legacy-cli-1.0.0.jar legacy-cli/src/main/resources/data/employees.csv legacy-cli/src/main/resources/data/events.csv

Refactor Assignment (suggested roadmap)
---------------------------------------
A) Java 8+
- Replace anonymous classes with lambdas & method references
- Use Stream API for stats/filtering/grouping
- Replace null-based APIs with Optional where appropriate
- Introduce functional interfaces for strategy-like logic

B) Java 9+
- Convert to JPMS modules (module-info.java), tighten encapsulation

C) Java 17+
- Convert DTO-like classes to records where sensible
- Consider sealed types for domain event hierarchy

D) Java 21–25
- Replace thread-per-task patterns with virtual threads where beneficial
- Introduce structured concurrency for the ingestion + report pipeline

Guardrails
----------
- Do not change observable behavior: unit tests should remain green.
- Keep backwards-compatible CLI arguments.
- Keep module boundaries clean: core should not depend on CLI.
