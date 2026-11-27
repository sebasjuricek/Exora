# Exora contributor guide

- Target Minecraft 1.21.1, NeoForge 21.1, and Java 21. Use the checked-in Gradle wrapper.
- Start with the [development handoff](docs/HANDOFF.md) for the active milestone and open checks. Use [CONTRIBUTING.md](CONTRIBUTING.md) for setup and development commands.
- Start any design or implementation chat by reading the [confirmed end goals](docs/MASTER_DESIGN.md#confirmed-end-goals), [campaign goals](docs/MASTER_DESIGN.md#campaign-goals) and [eight-age progression spec](docs/PROGRESSION.md). Use the [content catalog](CONTENT_CATALOG.md) for each item or capability's first access, way to obtain it, and intended use. Introduce equipment throughout the ages and give each age an engineering payoff that remains useful afterward.
- Treat the listed technology families and Overworld-first scope as confirmed. Treat specific tiers, recipes, names, implementation choices, and balance as proposals. Gate progress on recoverable proofs of capability using current or earlier unlocks. Keep optional military payloads out of required advancement and model combat, nuclear, and biotech systems as fictional gameplay.
- Read [ARCHITECTURE.md](ARCHITECTURE.md) before working across systems, [ROADMAP.md](ROADMAP.md) before scheduling work, and [REFERENCES.md](docs/REFERENCES.md) before proposing reuse or an external dependency.
- Use `exora` for mod and resource IDs and `io.github.sebasjuricek.exora` for Java. Treat released registry IDs and saved formats as compatibility contracts. Plan a migration before changing them.
- Use existing NeoForge registration, data generation, and capability APIs where applicable. Keep rendering, screens, and input handling in client packages. Validate game state changes on the logical server.
- Build with `.\gradlew.bat build` on Windows or `./gradlew build` elsewhere. For gameplay changes, exercise the affected flow in a development world. Check dedicated server behavior when touching common code, saves, networking, or multiplayer progression.
- Keep source, assets, and intended generated resources in Git. Keep local runs, saves, caches, and player data out of Git.
- Update affected design docs when a progression gate, ownership boundary, or planned content milestone changes. Keep examples, names, and assets original.
