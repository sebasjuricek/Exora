# Exora development handoff

Use this as the starting point for a new contributor or coding chat. The [master design](MASTER_DESIGN.md), [progression spec](PROGRESSION.md), and [roadmap](../ROADMAP.md) hold the project decisions and acceptance criteria. Update this handoff when the active milestone or verification status changes.

## Current state

- Exora targets Minecraft 1.21.1, NeoForge 21.1, and Java 21. The mod and resource namespace is `exora`. The Java package is `io.github.sebasjuricek.exora`.
- The repository contains a loadable mod entry point, package boundaries, and build tooling. Gameplay implementation starts with Workshop. GitHub Actions builds the JAR. `runData` is a loader smoke check. Interactive client, dedicated-server world, and multiplayer gameplay checks are pending.
- The confirmed scope includes nine technology families. The proposed eight-age campaign completes its Overworld finale in Fusion before orbital and planetary industry. See the [catalog](../CONTENT_CATALOG.md) for named features and their proposed unlocks.
- Exora's current scaffold has no additional runtime mod dependencies. The [source reviews](REFERENCES.md) document later candidates and their permissions and compatibility constraints.

## First playable milestone

Follow [Workshop](PROGRESSION.md#1-workshop). Keep the first change centered on one complete path from common material stock to a useful part.

1. Establish one reusable material form with a vanilla-input recipe.
2. Run one processing job on a starter generator and powered processor. Give players visible input, output, and power status so they can correct a blocked job.
3. Record the completed job as the Workshop proof that unlocks Electrical. Test the starter flow in a fresh survival world and ensure the proof persists through a server restart.

Use the [architecture](../ARCHITECTURE.md) when deciding where the power transfer, machine state, and team proof belong. Later ages can extend the same APIs once this flow works.

## Before expanding

| Decision or check | When it matters |
| --- | --- |
| First part and recipe | Choose one useful output and record the proposed input and recipe in the gameplay change. Exact costs and rates can be tuned after the full loop runs. |
| Save and multiplayer behavior | Before the first release with progression, test blocked output, power loss, restart, and two players using a station or claiming the same proof. Treat released IDs and save formats as stable. |
| Playtest setup | Use a disposable client world and a dedicated development server. The server operator handles the Minecraft EULA. Keep release saves and the CurseForge play instance separate from the development runs. |
| Licensing and assets | The repository is public and the mod metadata currently says All Rights Reserved. Make a licensing decision before inviting outside contributions or broad redistribution. For any proposed reused asset, check individual rights and provenance first. |
| External libraries | Workshop can proceed with NeoForge. Evaluate an animation library when a concrete asset needs it and storage interoperability when there's a concrete transfer use case. Verify client, server, and save behavior before making an integration part of the default build. |

For a fresh chat, point it at this file and `AGENTS.md`, then state which milestone you want to work on. Ask it to check the working tree and recent commits before treating this summary as current.
