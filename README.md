# Exora

Exora is an industrial science-fiction technology mod for Minecraft 1.21.1. Players build an automated supply chain, research successive technology ages, and use their equipment to explore hazardous sites and take on original encounters. The first expansion takes place in the Overworld.

## Development

The current project contains a loadable mod entry point, build tooling, and design documents. Gameplay content starts with the first industrial progression slice.

Use a 64-bit JDK 21. Open this directory as a Gradle project in your IDE. The Gradle wrapper supplies Gradle 9.2.1 and the build pins NeoForge 21.1.267 with ModDevGradle 2.0.148. Keep development runs separate from your CurseForge instance and existing worlds.

| Task | Windows PowerShell | Linux or macOS |
| --- | --- | --- |
| Build and check | `.\gradlew.bat build` | `./gradlew build` |
| Launch development client | `.\gradlew.bat runClient` | `./gradlew runClient` |
| Launch development server | `.\gradlew.bat runServer` | `./gradlew runServer` |
| Run data generation when providers are added | `.\gradlew.bat runData` | `./gradlew runData` |

The server uses `run-server/` and requires the operator to review and accept the Minecraft EULA before it can start a world. Build output is in `build/libs/`. GitHub Actions validates the wrapper, builds on JDK 21 on Linux, and uploads the mod JAR under the run's artifacts.

## Project documents

- [AGENTS.md](AGENTS.md) records contributor conventions.
- [MASTER_DESIGN.md](MASTER_DESIGN.md) defines the player experience and shared systems.
- [ROADMAP.md](ROADMAP.md) defines milestones and acceptance criteria.
- [ARCHITECTURE.md](ARCHITECTURE.md) defines package ownership, persistence, and networking boundaries.
- [PROGRESSION.md](PROGRESSION.md) defines the technology tree and encounter gates.
- [CONTENT_CATALOG.md](CONTENT_CATALOG.md) records planned content by age.
- [REFERENCES.md](REFERENCES.md) records upstream tooling and design influences.

Exora's mod metadata uses All Rights Reserved. The inherited NeoForge template files carry their original [MIT notice](TEMPLATE_LICENSE.txt).
