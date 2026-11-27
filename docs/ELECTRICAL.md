# Electrical age design brief

Electrical is the next playable age after Workshop. Its theme is making the shop do useful work at a distance. Keep the Workshop generator, portable Energy Buffer, Coil Mill and Line Meter in play. Use familiar stock before introducing new components. The exact names, costs, rates and block lineup are proposals to refine during implementation and survival testing.

## Survival route

| Stage | Player builds and uses | Proof |
| --- | --- | --- |
| 2.1 Local Grid | Make insulated copper cable from Workshop windings and common insulating stock. Run it between the existing Energy Buffer and Coil Mill with at least one cable block between the machines. Add a switch and use the Line Meter or a grid display to diagnose a disconnected line. | Complete a real mill job on a cable-fed supply. The entry recipes are available as soon as Workshop unlocks Electrical. |
| 2.2 Production Line | Feed familiar stock from a container into a small powered assembler and send the result to another container. Build a Relay Module from reused Workshop stock and redstone. Vanilla hoppers are a valid starting point. Add only as much Exora item routing as the line needs. | Demonstrate a real batch with automatic input and output. The server credits the team for the machine operation and the transfer. Crafting or inserting an equivalent item by hand cannot supply that proof. |
| 2.3 Survey Network | Install the factory-made module in an Overworld Survey Terminal. Put a Survey Mast at a second location and run a cable to it. Both endpoints can be made and deployed by the player. An authored survey site can provide the same objective later. | Finish the First Signal calibration with a working terminal and mast. Register the survey in the team ledger to unlock Mechanized. |

Ordinary Overworld resources cover the mandatory route. The setup from one stage remains useful in the next. Keep proof requirements visible in the bench or its successor, including the current fault, how to fix it, and the equipment a successful run opens. Let players collect a fresh batch or replace damaged equipment whenever they need to retry.

Deliver an early civilian use for the line in 2.2. A small filtration station can turn ordinary stock into a replaceable cartridge for a powered irrigator. Give the player one observable result on vanilla farmland and a reason to keep the station supplied. Place this branch alongside the Relay Module route as a useful option. It establishes the filtration and agriculture patterns used later in the Atomic campus and planetary habitats. The [content catalog](../CONTENT_CATALOG.md#civilian-technology) describes their longer use.

## Signature moment — First Signal

Stand at the terminal and close the switch. Give the mast a distinctive startup as the line supplies real power. Three visible segments wake up in sequence and a short radio chirp confirms the link. The terminal shows the same phases in text and reports an open switch, disconnected mast or power shortage in plain language. Use one short calibration run that can resume or be retried after a fault. The crew can watch from either endpoint and share the successful result.

At the end, the terminal records the mast as a waypoint. A craftable or reissuable Survey Chart gives a bearing and distance back to the mast, so players can use the station as a field landmark on future trips. Keep this first survey local and deterministic. Discovery of rare terrain or ore is unnecessary for the required route.

The line keeps making stock after calibration. The Relay Module has a second job as an input for future Mechanized controllers. The calibrated waypoint is a prototype for a fleet depot, where a later carrier can report a verified delivery. Mechanized begins with those demonstrated capabilities available.

## Implementation priorities

- Keep Workshop saves, IDs, proof rules and direct adjacency working. Extend the current team and personal ledgers with a compatible save path. Credit each proof once on the server and document how two teammates cooperate on the same installation.
- Keep energy and item transfers on the established NeoForge capability interfaces. Bound local routing and handle loops, broken paths, chunk unloads and power loss. Charge, item batches and progress survive normal saves and reconnects.
- Build the route in the order above. For each stage, run the wrapper and prove it in a disposable survival world using current or earlier unlocks. Keep the stage small enough to test end to end before adding side equipment.
- Verify the original Workshop route or an existing Workshop save, cable and switch recovery, a blocked production output, a server restart during work, and shared credit on a dedicated server. Record which checks ran and where work stopped in the [development handoff](HANDOFF.md).

## Implementation prompt

```text
Design and implement the full Electrical age for Exora. Find the GitHub source checkout and read AGENTS.md, docs/HANDOFF.md, docs/ELECTRICAL.md, CONTENT_CATALOG.md, docs/MASTER_DESIGN.md, docs/PROGRESSION.md and ARCHITECTURE.md. Check git status and run the checked-in Gradle wrapper first. Keep Minecraft 1.21.1, NeoForge and Java 21. Use disposable worlds and preserve existing Workshop saves, IDs and progression.

Electrical should turn my Workshop into a factory that can do work at a distance. Reuse the generator, buffer, mill, Line Meter and Workshop stock. Before editing, map when I get each new item, how I make or earn it, what I first use it for and how I replace it. Check that each mandatory proof is reachable with the equipment already available. Then build and test in this order.

1. Local Grid. Make affordable insulated copper cable and a switch. Run an actual Coil Mill batch through at least one cable block from the existing Energy Buffer. Show an open switch, broken connection and low supply as distinct problems I can diagnose and fix.
2. Production Line. Build a compact assembler and use it to move Workshop stock from an input container through a real powered job into an output container. Hoppers are fine. The resulting Relay Module is used in 2.3 and later supplies Mechanized controllers. Award the proof from an actual automated input, job and output. Add one optional civilian use for the line, such as a replaceable filter cartridge and powered irrigator that visibly helps a small plot.
3. Survey Network. Build a Survey Terminal and a separate cable-powered mast, install the factory-made module, and commission First Signal. Closing the switch wakes the mast in visible stages and finishes with a short radio chirp. The terminal shows progress and recovery steps whenever the link stalls. Completing the link earns the team its Mechanized unlock and a reissuable Survey Chart that gives bearing and distance back to the mast. The route must work in an ordinary Overworld without rare generated sites.

Give every stage a practical use before the next unlock. Keep the current objective, requirements and future use visible in the bench or its successor. Keep transfers, inventories and proof credit server-authoritative. Handle power loss, cable loops, chunk unloads, output blockage, saves and restarts. Build for solo and shared teams. End this work at the Mechanized unlock.

Build and playtest each stage. Verify the Workshop-to-Electrical handoff in an existing Workshop save and exercise the full route from ordinary survival resources. Check cable and switch recovery, blocked output, restart during a job and a shared proof on a dedicated server with two clients if available. Add focused regression tests. Update the catalog, progression and handoff to reflect what shipped and which checks ran. Finish with the JAR path, exact dev-client command and short survival walkthrough.
```
