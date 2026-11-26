# Exora master design

Exora is an industrial technology and science-fiction Minecraft mod. Players grow a small Overworld workshop into an automated industrial network, build and maintain equipment, and use it to solve authored challenges. A completed Overworld campaign leads to orbital infrastructure, planets, and eventually dimensional and stellar projects.

## Confirmed end goals

These families define Exora's intended scope. Individual recipes, names, statistics, exact unlocks, and delivery dates are proposals until playtesting. The first playable age is Workshop. Later ages build on its production and research contracts.

| Family | End goal |
| --- | --- |
| Military and science fiction | Fictional weapons, energy blades, armored vehicles including tanks, fighter planes, fighter jets, helicopters, pilotable mechs, energy shields, fictional nukes, missiles, bombs, and advanced field equipment. Use gameplay abstractions for fictional payloads and give servers control over permissions and world effects. |
| Powersuits | Exoskeletons, modular powersuits, atomic-themed armor, nanotech suits, quantum armor, and jetpacks. Players fit, charge, service, and upgrade gear for different environments and tasks. |
| Transportation | Cars, trucks, trains, planes, ships, submarines, hovercraft, and maglev systems. Different transport modes serve cargo, exploration, access, or rescue needs. |
| Robotics | Androids, drones, robotic companions, automated construction, and artificial intelligence for bounded in-game jobs. Robots draw energy, parts, and assignments from the player's infrastructure. |
| Computing | Computers, servers, supercomputers, quantum computers, digital storage, and industrial control networks. Machines can be monitored and coordinated at increasing scale. |
| Scientific research | Atomic sciences, particle physics, chemistry, material sciences, biotechnology, cybernetics, and nanotechnology. Fictional instruments and experiments unlock tangible factory and expedition capabilities. |
| Space | Rockets, satellites, spaceships, space stations, planetary colonies, asteroid mining, and terraforming. Off-world industry needs cargo, power, shelter, return routes, and maintenance. |
| Futuristic technology | Teleportation, gravity manipulation, matter replication, holograms, dimensional travel, artificial wormholes, and Dyson spheres. Late industry extends logistics to new environments and supports repeatable megaprojects. |
| Civilian technology | Automated agriculture, medical chambers, environmental purification, advanced construction, and industrial infrastructure. These systems support recovery, habitat operation, and resilient production. |

The [content catalog](../CONTENT_CATALOG.md) connects each family to a proposed fabrication or research milestone. The [progression spec](PROGRESSION.md) defines the eight ages, internal tiers, and verifiable gates. The [roadmap](../ROADMAP.md) orders development independently of in-game progression.

## Player loop

**Build → process → automate → research → equip → explore.** The first bench, press and generator are player-built from ordinary survival stock. The opening culminates in a commissioned Coil Mill and portable stored power. [Workshop field guide](WORKSHOP.md)

Field discoveries and demonstrated production feed research. Research offers a capability the player can manufacture, put to work, and service. Each age adds a useful way to solve a factory or expedition problem. Earlier materials remain useful as stock, spare parts, or remote infrastructure.

Use GregTech-inspired sequential ages and reusable material forms. Use HBM-inspired manufacturing with visible machine states, transport, protected facilities, and field equipment. Keep the recipe graph legible. Reuse plates, frames, drives, controllers, and energy cells across the factory. Increase complexity through new processes, installations, and automation.

Give each age one memorable engineering accomplishment that the player builds toward, can see working, and can use afterward. Let those moments grow with the scale of the technology. Workshop's mill runs on saved charge, Electrical's First Signal lights a mast from a shop-built grid, Mechanized can complete a repeatable cargo delivery, and the first orbital trip can return the crew safely. Prioritize a clear action and a useful result when designing each age's payoff. [Electrical design brief](ELECTRICAL.md)

## How the systems connect

- **Research and access.** A workshop ledger grows into a terminal that shows each age gate, unmet capabilities, and recovery options. Shared proofs unlock team designs. The server owns access and permissions. Required encounters use deterministic evidence and repeatable objectives. Optional challenges expand routes, loadouts, and efficiency.
- **Manufacturing, power, and logistics.** Processing lines make shared stock and specialized modules. Power generation, storage, and distribution feed those lines and field chargers. Logistics handles materials, supplies, and return cargo. Operators can see why a machine stopped and recover it without losing already earned research.
- **Computing and robotics.** Filters and local controls give way to networks, servers, robots, and higher-end computing. Power, parts, and job limits give automated systems operating costs. Vehicles and machines keep essential local controls during a network outage.
- **Equipment and mobility.** Research provides designs. Fabrication and service stations produce frames and modules. Players choose protection, utility, or mobility, then fit, operate, maintain, and recover their equipment. Salvage, spare parts, field repairs, and rescue systems close the loop with the factory.
- **Encounters and environments.** Original entities, ruins, industrial hazards, and bosses challenge protection, transport, repair, and supply. Balance equipment against Exora encounters. Use fictional mechanics for weapons, nuclear systems, and biotechnology. Gate the mainline on work that teams can complete with current-age capabilities, including when optional payloads are disabled.
- **Off-world industry.** The Fusion finale completes the Overworld campaign and proves return and rescue readiness. Orbital industry comes next, then planetary settlement, then dimensional routes and stellar infrastructure. Each new destination has a way to resupply and get home.

## Presentation and performance

Use original models, readable interfaces, and effects that explain state. Include cues alongside color and sound and allow visual effects to scale. Bound ticking, pathfinding, simulation, and networking. Profile a running factory with several players before scaling networks, fleets, or worlds. Use separate rendering and server simulation responsibilities as defined in the [architecture](../ARCHITECTURE.md).

Check [upstream references and reuse boundaries](REFERENCES.md) before proposing an imported asset or dependency.
