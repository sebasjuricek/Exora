# Exora architecture

The single Gradle module targets Minecraft 1.21.1 with NeoForge and Java 21. `Exora` is the shared mod entry point. Use feature-local registration as content is added and compose it from the entry point. The current package files document intended ownership.

| Package under `io.github.sebasjuricek.exora` | Owns |
| --- | --- |
| `materials` | Material identifiers, forms, and common tags |
| `machines` | Processing, recipe types, block entities, inventories, and multiblocks |
| `energy` | Power generation, storage, transfer, and machine-facing power contracts |
| `logistics` | Item and fluid transport, filters, and routing |
| `progression` | Research definitions, prerequisites, unlock records, and team milestones |
| `equipment` | Tools, powersuits, armaments, modules, and equipment state |
| `vehicles` | Frames, controls, module slots, ownership, persistent state, and service stations |
| `entities` | Robots, hostile entities, and boss behavior |
| `world` | Structure placement, discovery sites, and environmental hazards |
| `client.rendering` | Models, entity renderers, animations, particles, and other visual effects |
| `client.screen` | Screens, overlays, and client input presentation |

## Integration

Machines consume material tags and expose supported capabilities. Logistics moves resources through these capabilities. Energy owns power behavior. Equipment and vehicles use parts and power through their own lifecycle code. Progression owns unlock checks used by manufacturing and workstation actions. Entities and world encounters award discovery evidence through a progression interface. Keep shared contracts near the feature that owns their behavior and avoid dependency cycles.

## Multiplayer and data

- The logical server owns inventory changes, research progress, vehicle authority, and damage. Clients send intent. Validate the sender's permissions, distance, current state, and control seat before applying changes.
- Sync bounded snapshots when a player begins tracking or opens a screen. Send small changes while relevant. Keep dedicated-server code free of client-only classes.
- Use data packs for declarative recipes, tags, and unlock definitions. Keep original assets under `assets/exora/` and gameplay data under `data/exora/`. Write data generation output to `src/generated/resources/`.
- Plan for machine state on block entities, item state in item data components, vehicle state on entities, and team research in world saved data. Store stable identifiers and version custom formats when they begin to evolve.
- Bound per-tick routing and simulation, reuse cached results with explicit invalidation, and avoid loading chunks to complete routine processing. Exercise save/reload, player disconnect, chunk unload, and concurrent use as features are added.
