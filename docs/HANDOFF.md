# Exora development handoff

Workshop is implemented in the source checkout containing this file. The baseline was clean at `e1da953`. Development used the checked-in wrapper, Minecraft 1.21.1, NeoForge 21.1.267 and JDK 21. Gameplay and tests use the existing NeoForge dependencies. The [survival walkthrough](WORKSHOP.md) lists every required recipe, the optional shop fittings, the FE costs and the team rules.

## What players can do

1. Craft the Workshop Bench and Stone Mallet from ordinary stock. Assemble the first Timber Frame to unlock the Hand Press.
2. Turn iron into plates and shafts, build a drive and fit it to the press. A visible flywheel, faster return and four-batch strokes reward the installation. Spare stock also makes lanterns and hoppers.
3. Build a Starter Generator and Coil Mill. Make two Wound Cores from a winding and collect them from the tray. The saved ledger opens Electrical. A Line Meter lets a player compare the charge on any supported face.
4. Build and charge an Energy Buffer. It can move its charge with a pickaxe and operate the same mill away from the generator.

## Development entry points

- `machines/bench` implements the server-authoritative ledger menu and manual operations. Bench and press plans live in `data/exora/recipe/manual`. Each proof opens the ingredients and equipment needed for the following step.
- `energy` owns generation, storage, transfer and station menus. `machines/powered` snapshots each consumed workpiece and its output. The three mill recipes live in `data/exora/recipe/milling`. Client screens and original models use the saved machine state.
- `progression/WorkshopProgress` stores consecutive proofs for scoreboard teams and personal UUIDs in Overworld SavedData. A legitimate mill batch can earn one credit. An automated line can keep producing after that demonstration.
- Start the next gameplay milestone with the [Electrical design brief](ELECTRICAL.md). Implement Local Grid first by powering the existing mill from the existing buffer through cable. Preserve the direct adjacent-generator Workshop proof. The full age adds automated production and ends with First Signal at a player-built survey mast. See the [architecture](../ARCHITECTURE.md), [progression spec](PROGRESSION.md) and [roadmap](../ROADMAP.md) for the wider plan.
- Use the [content catalog](../CONTENT_CATALOG.md) to place equipment when the player first has a use for it, from Electrical survey gear to Mechanized carriers and later orbital infrastructure. Each entry records first access, acquisition and a practical job. The [campaign goals](MASTER_DESIGN.md#campaign-goals) explain what earlier systems should grow into. The Electrical brief includes the [next Codex prompt](ELECTRICAL.md#implementation-prompt).

## Verification

The wrapper built the original scaffold and the bench, press and power implementation. The opt-in headless run passed seven dedicated-server GameTests. They exercise shared proof visibility, blocked manual operations, exact ingredient consumption, power failure, generator and mill state reconstruction, one-input teardown refunds, competing collectors, hopper operation, isolated stored power and charged-buffer harvesting. The test source set and its world template are excluded from the release JAR.

The live fixture starts two real NeoForge clients in survival on one loopback dedicated server. It provides a harvestable oak log and a chest of ordinary raw materials to make the route repeatable. The operators use actual client interactions for crafting, smelting, placement, menu operations and collection. Their separate development directories hold the logs and screenshots. The recipe graph was also checked for tier reachability and downstream use of every intermediate. Exploration time and ore-finding balance still need a human survival session.

## Last verified point

On the live server, Fitter harvested a log, crafted the opening equipment, furnace-smelted stock, assembled the frame, fitted the press drive and made a four-batch stroke. Winder's open bench showed the shared proofs. After a restart Fitter built both powered machines and loaded two windings. With no fuel the mill retained the first workpiece at zero progress. The server then saved and shut down with the generator burning and the mill at 24 of 80 ticks. After startup the first batch finished with the next winding held behind its occupied tray. Winder collected the two cores and both clients received Electrical. Clearing the tray released the queued job.

Fitter built the buffer with Winder's cores, charged it, harvested it with a stone pickaxe, removed the generator and placed the charged buffer beside the mill. Processing one iron ingot produced three plates. The resulting buffer held 3,904 of 4,096 FE, and the generator was in Fitter's inventory. Fitter also built the optional Line Meter and hopper and used the meter. After another clean server restart, each operator reopened the bench at three proofs. Leaving the scoreboard team gave Winder a fresh personal ledger and rejoining restored the completed team ledger.

The final wrapper build and all seven GameTests passed. The release JAR was compared byte-for-byte against the built main classes and intended model, texture, recipe and language resources. Dedicated test fixtures are excluded. The build also ran data generation. The optional lantern plan was checked against the recipe graph. The live route used a supplied chest for bulk raw materials and scripted inputs on two real clients. For first-time pacing feedback, the next playtest is an unaided run in a random survival seed.

Reproduce the [two-client sequence](../src/playtest/scenarios/README.md) or run `.\gradlew.bat -PworkshopPlaytest runWorkshopGameTest` from the checkout. Use `.\gradlew.bat runClient` for normal play. The release artifact is `build/libs/exora-0.1.0-SNAPSHOT.jar`.
