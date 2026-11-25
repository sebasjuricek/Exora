# Exora

[![Build](https://github.com/sebasjuricek/Exora/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/sebasjuricek/Exora/actions/workflows/build.yml)

Exora is an industrial science-fiction mod for Minecraft 1.21.1. Players grow an Overworld workshop into an automated factory, build and maintain equipment, and use it to explore, solve problems, and take on original encounters. Later ages extend the factory into orbit, planets, and dimensions.

**Development status.** The NeoForge scaffold and design are in place. The first gameplay milestone is a Workshop production loop that manufactures a useful part and unlocks Electrical.

## Start here

| If you want to… | Read |
| --- | --- |
| Build or run the project | [Contributing](CONTRIBUTING.md) |
| Pick up development or start a new coding chat | [Development handoff](docs/HANDOFF.md) and [AGENTS.md](AGENTS.md) |
| Review Exora's confirmed end goals | [Master design](docs/MASTER_DESIGN.md#confirmed-end-goals) |
| Trace the eight ages and their unlocks | [Progression specification](docs/PROGRESSION.md) and [content catalog](CONTENT_CATALOG.md) |
| Review the next milestone or a system boundary | [Roadmap](ROADMAP.md) and [architecture](ARCHITECTURE.md) |
| Evaluate an upstream asset, mod, or dependency | [Reference reviews](docs/REFERENCES.md) |

## Quick build

Install a 64-bit JDK 21 and run the checked-in wrapper from the directory containing `settings.gradle`. On Windows, use `.\gradlew.bat build`. On Linux or macOS, use `./gradlew build`. The JAR is written to `build/libs/`, and successful GitHub Actions runs publish it as `exora-jar`.

Exora's mod metadata specifies All Rights Reserved. The inherited NeoForge template files carry their original [MIT notice](TEMPLATE_LICENSE.txt).
