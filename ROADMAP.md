# Exora roadmap

Deliver one complete survival loop at a time. The eight ages and their internal gates are defined in the [progression spec](docs/PROGRESSION.md). Milestones below describe development acceptance. Gameplay milestones and their content placements are proposed.

| Milestone | Scope | Acceptance |
| --- | --- | --- |
| Foundation | NeoForge scaffold, GitHub build, and design baseline | A fresh checkout builds with Java 21. CI publishes the mod JAR. The README and contributor guide lead to the design, progression, and references. |
| 1. Workshop | Playable bench, metalworking and commissioning chapter | Assemble a frame, gear a press, claim a mill-made core batch and use the unlocked Energy Buffer to run the mill. [Survival route](docs/WORKSHOP.md) and [verification record](docs/HANDOFF.md). |
| 2. Electrical | The Energy Buffer is playable. Extend Local Grid, Production Line and Survey Network | Automate input and output through the power and logistics interfaces. Diagnose blocked outputs and power loss. Restore the survey station using a factory-made batch. |
| 3. Mechanized | Precision production, first carrier and service bay, and Industrial Control | Fabricate, operate, load, repair, and recover a carrier. Route a job through an industrial controller. Two players can complete and share the relay proof. Add vehicle variants after this loop works. |
| 4. Aerospace | Flight, Expedition Systems, and Remote Science | One basic aircraft can take off, land, and be serviced in multiplayer. A team can recover a failed flight and restore the remote beacon. Extend to specialized aircraft and other transport once controls and servicing work. |
| 5. Atomic | Containment, Shielded Power, and Remediation | Operate a fictional generator and identify and recover from an in-game fault. Clear the campus with deployed protection or remote service. The mainline passes with optional payloads disabled. |
| 6. Fusion | High-Energy Industry, Adaptive Systems, and Overworld Mastery | Sustain fusion-powered production and service one suit or robot. Complete the original boss chain, recover from a wipe, and prove that the factory can resupply and support an expedition. Close the Overworld campaign before beginning Age 7. |
| 7. Orbital and Planetary | Low Orbit, Planetary Logistics, and Habitats | Repeat a launch, delivery, and return. Recover an off-world material, resupply a colony, and verify a controlled habitat change. Test rescue after a missed return. |
| 8. Cosmic Engineering | Field Control, Dimensional Industry, and Stellar Megaprojects | Verify a reversible journey and commission one Dyson sphere sector that powers an actual off-world endpoint. Verify restart, loss of a route, and reconstruction. |

## Acceptance across milestones

- Run the mandatory path in fresh solo survival and on a dedicated server with a team. Check proof visibility, deterministic evidence, retries, reconnect, save/restart, and whether every requirement is available in the active age.
- Check that ongoing production can be automated after its first successful demonstration. Test blocked outputs, loss of power, partial deliveries, chunk unload, and shared use. Make the cause visible in the relevant UI.
- Test vehicle and expedition recovery while a player or team is separated from the base. Exercise a viable return route before each new environment becomes mandatory.
- Test permission boundaries and optional-content settings. Keep rendering, tick work, synchronization, and pathfinding within a measured budget as factories and fleets grow.
- Deliver at least one representative machine, vehicle, suit, or robot through its full lifecycle before expanding its family from the [content catalog](CONTENT_CATALOG.md). Review reuse and compatibility in the [references](docs/REFERENCES.md) before adopting a dependency or asset.
