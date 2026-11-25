# Contributing to Exora

Use a 64-bit JDK 21. Check `java -version` and confirm your IDE uses JDK 21 for Gradle. Open the directory containing `settings.gradle` as a Gradle project. If your checkout is nested in a CurseForge instance, open the source repository itself and use separate development worlds.

The wrapper pins Gradle 9.2.1. The build targets Minecraft 1.21.1 and pins NeoForge 21.1.267 and ModDevGradle 2.0.148. See [gradle.properties](gradle.properties) and [build.gradle](build.gradle) for the current pins.

| Task | Windows PowerShell | Linux or macOS |
| --- | --- | --- |
| Build and check | `.\gradlew.bat build` | `./gradlew build` |
| Launch development client | `.\gradlew.bat runClient` | `./gradlew runClient` |
| Launch development server | `.\gradlew.bat runServer` | `./gradlew runServer` |
| Run data generation | `.\gradlew.bat runData` | `./gradlew runData` |

The development server uses `run-server/` and can start in offline mode. For a local smoke test, create or edit `run-server/server.properties` and set `server-ip=127.0.0.1` before launching. For shared playtests, use an authenticated NeoForge server with the built JAR and follow the server setup instructions, including the operator's EULA review. [NeoForge server setup](https://docs.neoforged.net/user/docs/server/)

Data generation uses `run-data/` and writes generated resources under `src/generated/resources/`. Commit intended generated resources with the corresponding source. Keep local worlds, logs, and caches outside Git.

## Develop and review

1. Use the [handoff](docs/HANDOFF.md), [roadmap](ROADMAP.md), and [contributor conventions](AGENTS.md) to choose a small change. For a change going through GitHub review, use a short-lived branch from `main`.
2. Run the build. For changes that players can interact with, exercise the changed flow in a disposable world. For changes to common code, persistence, networking, or shared progression, cover dedicated-server behavior as well.
3. In the pull request, describe the player-visible change, checks you ran, and any effect on saves, registry IDs, or dependencies. Link to the affected design or roadmap entry when relevant. [GitHub's pull request template instructions](https://docs.github.com/en/communities/using-templates-to-encourage-useful-issues-and-pull-requests/creating-a-pull-request-template-for-your-repository)
4. Check GitHub Actions. CI runs the Linux JDK 21 build and uploads the JAR. Use the pull request to record any in-game checks alongside CI results.
