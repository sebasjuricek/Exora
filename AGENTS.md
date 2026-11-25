# Exora contributor guide

- Target Minecraft 1.21.1, NeoForge 21.1, and Java 21. Use the checked-in Gradle wrapper.
- Read [MASTER_DESIGN.md](MASTER_DESIGN.md) and [ARCHITECTURE.md](ARCHITECTURE.md) before working across systems. Use [PROGRESSION.md](PROGRESSION.md) and [CONTENT_CATALOG.md](CONTENT_CATALOG.md) when adding content or changing unlocks.
- Use `exora` for mod and resource IDs and `io.github.sebasjuricek.exora` for Java. Treat released registry IDs and saved formats as compatibility contracts. Plan a migration before changing them.
- Use existing NeoForge registration, data generation, and capability APIs where applicable. Keep rendering, screens, and input handling in client packages. Validate game state changes on the logical server.
- Build with `.\gradlew.bat build` on Windows or `./gradlew build` elsewhere. For gameplay changes, exercise the affected flow in a development world. Check dedicated server behavior when touching common code, saves, networking, or multiplayer progression.
- Keep source, assets, and intended generated resources in Git. Keep local runs, saves, caches, and player data out of Git.
- Update affected design docs when a progression gate, ownership boundary, or planned content milestone changes. Keep examples, names, and assets original.
