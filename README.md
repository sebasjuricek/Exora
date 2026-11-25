# Exora

Exora is an industrial science-fiction technology mod for Minecraft 1.21.1. Players build an automated supply chain, research successive technology ages, and use their equipment to explore hazardous sites and take on original encounters. The first expansion takes place in the Overworld.

## Design goals

The end goal spans fictional military and science-fiction equipment, powersuits, transportation, robotics, computing, scientific research, space infrastructure, futuristic technology, and civilian technology. The eight-age campaign starts with a workshop and ends with dimensional routes and a Dyson sphere sector.

Start with the [confirmed end goals](docs/MASTER_DESIGN.md#confirmed-end-goals), then follow the [eight-age progression spec](docs/PROGRESSION.md), [content catalog](CONTENT_CATALOG.md), and [roadmap](ROADMAP.md). The catalog maps each requested family to a proposed unlock. Gameplay development is at the foundation stage.

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
- [docs/MASTER_DESIGN.md](docs/MASTER_DESIGN.md) defines the confirmed end goals, player experience, and shared systems.
- [ROADMAP.md](ROADMAP.md) defines milestones and acceptance criteria.
- [ARCHITECTURE.md](ARCHITECTURE.md) defines package ownership, persistence, and networking boundaries.
- [docs/PROGRESSION.md](docs/PROGRESSION.md) defines all eight ages, their internal tiers, the technology branches, and advancement proofs.
- [CONTENT_CATALOG.md](CONTENT_CATALOG.md) records planned content by age.
- [docs/REFERENCES.md](docs/REFERENCES.md) records the upstream source reviews, reuse decisions, and integration plan.

Exora's mod metadata uses All Rights Reserved. The inherited NeoForge template files carry their original [MIT notice](TEMPLATE_LICENSE.txt).
