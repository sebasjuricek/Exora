# Exora technological progression

Start with the [master design](MASTER_DESIGN.md) for the confirmed scope, then use this document to trace how players unlock those technologies. See the [content catalog](../CONTENT_CATALOG.md) for the unlock of each requested family and the [roadmap](../ROADMAP.md) for delivery order.

**Confirmed requirements.** Exora has eight sequential technological ages with internal tiers. Manufacturing is moderately complex, earlier production stays useful, automation replaces repeated manual work, and one readable mainline ties together the technology branches. Progression blends production with authored Overworld encounters before opening space and later dimensions. The technology families listed in the master design are confirmed end goals.

**Proposed specification.** Workshop and the first Electrical buffer follow the implemented route. Later tier placements, materials, sites, equipment and acceptance thresholds are design proposals. Every "required" input or proof describes the proposed default survival path. The Workshop guide lists the current costs and operating rates. Balance for later ages remains open.

## Mainline at a glance

| Age | Three internal tiers | Proof that advances the team |
| --- | --- | --- |
| [1. Workshop](#1-workshop) | 1.1 Workbench · 1.2 Metalwork · 1.3 Starter Power | Collect Wound Cores from a Coil Mill job powered by a Starter Generator |
| [2. Electrical](#2-electrical) | 2.1 Local Grid · 2.2 Production Line · 2.3 Survey Network | Install a factory-made Relay Module and complete the First Signal survey calibration |
| [3. Mechanized](#3-mechanized) | 3.1 Precision · 3.2 Surface Industry · 3.3 Industrial Control | Commission a service route and recover the relay record |
| [4. Aerospace](#4-aerospace) | 4.1 Flight · 4.2 Expedition Systems · 4.3 Remote Science | Restore the remote beacon and return its survey data |
| [5. Atomic](#5-atomic) | 5.1 Containment · 5.2 Shielded Power · 5.3 Remediation | Demonstrate shielded power and close the research campus hazard loop |
| [6. Fusion](#6-fusion) | 6.1 High-Energy Industry · 6.2 Adaptive Systems · 6.3 Overworld Mastery | Complete the Overworld finale and certify a resupply and rescue loop |
| [7. Orbital and Planetary](#7-orbital-and-planetary) | 7.1 Low Orbit · 7.2 Planetary Logistics · 7.3 Habitats | Sustain an off-world resupply and return an analyzed anomaly sample |
| [8. Cosmic Engineering](#8-cosmic-engineering) | 8.1 Field Control · 8.2 Dimensional Industry · 8.3 Stellar Megaprojects | Complete a reversible dimensional journey and commission a Dyson sphere sector |

Each tier unlocks the machinery and access needed to attempt its own proof. Completing that proof opens the next tier in the row. Completing the final tier proof opens the following age. A new age grants its entry designs before the next proof is offered. Branch sidegrades share the current tier and its inputs. The research UI displays one checklist per tier and previews the next age.

## Age payoffs

These are design targets for what the player will see working and what remains usable after the proof. The [content catalog](../CONTENT_CATALOG.md) gives the acquisition station, input or claim, and first use for each technology.

| Age | Moment the player earns | What remains useful |
| --- | --- | --- |
| Workshop | Disconnect the generator and watch the mill finish a batch on stored power. | A working bench, fabrication stock, mill and portable buffer. |
| Electrical | Close the switch and see the distant survey mast light up and confirm the first signal. | A production line, a local grid and a field waypoint you can navigate back to. |
| Mechanized | Unload the first verified freight delivery, then see the destination register on the control network. | A repeatable depot route, stocked service point and a carrier that can be repaired for the next run. |
| Aerospace | Land, service and send an aircraft back out. Bring a remote beacon online and view its survey on a tabletop projection. | A working hangar, arrival and return route, and a survey of places the crew can now reach. |
| Atomic | Isolate a simulated fault and reopen a section of the research campus by bringing its protective systems online. | A protected supply grid, serviceable field protection and a stocked recovery point. |
| Fusion | Complete the Overworld finale and recover a crew using equipment, spares and support built by the factory. | An expedition-ready supply and rescue network that supports the first launch. |
| Orbital and Planetary | Make a crewed trip to a dock and back, then give a planetary habitat its first working resupply and greenhouse. | An orbital transfer point, off-world feedstock and a functioning habitat. |
| Cosmic Engineering | Bring cargo home through a dimensional gate and run a remote endpoint from a commissioned stellar collector sector. | An expandable freight and power network across destinations. |

## Technology branches

These six branches share the age sequence. A branch can provide a required capability or an optional improvement at a given tier. The gate names the capability to prove so a fork such as air versus ground access stays a choice.

| Branch | How it develops | What other branches use |
| --- | --- | --- |
| Materials and Manufacturing | Reusable stock → precision parts → specialty composites → orbital and field materials | Frames and modules use shared stock. Research proves new material behavior through production. Advanced lines still consume plates and basic parts. |
| Power and Infrastructure | Local generation → distribution → protected grids → high-output and remote networks | Factories, chargers, controls, and habitats draw from the same generation and storage. Environmental systems provide cooling or recovery capacity in Minecraft terms. |
| Automation and Computing | Filters and conveyors → computers and servers → robotics and supercomputers → quantum control | Automation operates existing production. Fleet dispatch and research consume status and telemetry. Field devices keep local control when the network is down. |
| Mobility and Exploration | Survey and repair → surface routes → flight → orbit and planets → interdimensional routes | Vehicles deliver inputs, retrieve discoveries, and extend maintenance range. Hangars, stations, and gates require infrastructure built by the factory. |
| Equipment and Protection | Tools → exoskeletons and protected equipment → modular suits and mechs → nanotech and quantum systems | Field gear extends safe access and feeds discoveries back to research. Frames, chargers, repair stations, and suit modules share the manufacturing supply chain. |
| Research and Life Systems | Survey records → chemistry and material science → environmental recovery → life support and habitat engineering | Research verifies unlocks. Agriculture, purification, medicine, biotech, cybernetics, and environmental control improve factory resilience and support expeditions. |

### Where branches meet

- **Mechanized service route.** The Electrical grid and automated parts line feed precision manufacturing. A precision module earns access to the service bay. A charged cargo carrier proves the route, then the new control network and the relay discovery complete the age.
- **Overworld finale.** Atomic power and shielding start the Fusion fabricator. Its modules equip the expedition team. Repair and resupply infrastructure supports the boss encounter and proves the same supply loop needed to attempt orbit.
- **Off-world industry.** Agriculture, purification, and construction support habitats. Off-world cargo supplies new materials for the research lab. Field technology then feeds back into transport and habitat power as the team builds dimensional routes and stellar infrastructure.

### Progression and manufacturing rules

- **Place equipment where it has an immediate job.** Unlock entry tools before the proof they enable. Put stronger variants and specialized equipment after a successful first use. For each design record, name the unlock condition, the station or source, how to replace it, and the first job it handles.
- **Use a small shared part set.** Plates and frames provide structure. Drives provide motion. Controllers provide logic. Cells provide portable power. Add a specialty material or module when it grants a new property such as shielding, flight, or extreme-environment operation. Reuse the same forms across recipes and allow later lines to produce them automatically.
- **Make complexity come from the factory.** A job can need power, transport, packaging, or a machine environment. One demonstration batch can validate a tier. Subsequent batches use filters, routing, buffers, and machine controls. Earlier machines keep a throughput, cost, backup, or remote-work role.
- **Use observable evidence.** A team ledger records completed production, installations, and discovery objectives. Show missing conditions, where to find the input, and what completion grants. Use completed jobs and functional systems as gates. Leave real-time research timers, random mandatory drops, and repeated hand crafting out of the mainline.
- **Support recovery.** Sites provide a reset, a replacement terminal, or an equivalent verified discovery. Team evidence survives item loss and disconnects. Use clear approach and return routes, replacement parts, or rescue access before asking players to risk a new environment.
- **Keep branch equipment useful.** Published gate routes must use current or earlier unlocks. Optional combat payloads, specialized vehicles, and rare sidegrades provide playstyle choices and encounter rewards outside the mandatory path.
- **Share credit fairly.** The server records proofs for the expedition team. Solo players use the same path. The proposed flow validates actual production or completion, including approved batch outputs and registered recovery objectives.

## 1. Workshop

**Survival inputs.** Gather timber, cobblestone, iron, copper, charcoal and a little glass. Three hands-on proofs unlock plans at a shared bench. The chapter plays out wherever the player chooses to build their shop. [Walkthrough, full recipe graph and team rules](WORKSHOP.md)

| Tier | New capability | Proof and payoff |
| --- | --- | --- |
| 1.1 Workbench | Craft a Workshop Bench and reusable Stone Mallet. The ledger shows plans, stock on hand and the current objective. | Assemble a Timber Frame at the bench. Use the frame to build the Hand Press. |
| 1.2 Metalwork | Use two press dies for plates and shafts. Build casings, drives and copper windings. Use spare stock for lanterns and hoppers. | Fit a Drive Assembly to the press. Its flywheel enables faster, four-batch strokes and opens the generator and mill plans. |
| 1.3 Starter Power | Fuel the Starter Generator with charcoal. A touching Coil Mill turns stock into cores and makes plates and windings more efficiently. A Line Meter samples stored energy. | Take Wound Cores from a completed generator-powered job. The bench records commissioning and opens the Energy Buffer plan. |

**Electrical handoff.** Build a buffer with the commissioned cores. Charge it, disconnect the generator and run the mill from stored FE. Power loss pauses the current workpiece. An occupied tray holds back the next batch. Feed and extraction hoppers give the shop an ongoing production role.

## 2. Electrical

The [Electrical design brief](ELECTRICAL.md) describes the proposed minimum route, its player-built survey option, and the First Signal finale. The broader equipment below can be added as that route matures.

**Required inputs.** Workshop plates and drives, copper, redstone, glass, and insulating stock. Electricity begins with the Workshop generator and can use charcoal or an early renewable option when available.

| Tier | Machines, processes, and power | Unlocks and capability | Proof |
| --- | --- | --- | --- |
| 2.1 Local Grid | Cables, buffers, switches, chargers, and grid diagnostics. | Powered scanner and repair tool. Supply multiple devices from a local grid. | Run a machine from a networked buffer and read its power state. |
| 2.2 Production Line | Grinder or processor, basic assembler, conveyors, item filters, fluid pipes, and simple chemistry and water processing. | Automatically feed inputs and extract output. Start irrigation, automated planting and collection, and basic purification. | Demonstrate a batch with automated input and output. |
| 2.3 Survey Network | A Survey Terminal and separate cable-powered mast use the same standard parts as the factory. | Record a field waypoint and read its bearing on a Survey Chart. Resource mapping can follow as a side capability. | Install a factory-made Relay Module and complete First Signal calibration. |

**World and challenges.** A player-built station gives every survival world a repeatable route. An optional disabled Overworld station can teach field repair through the same proof. Power isolation and blocked production outputs teach fault diagnosis along the way.

**Advance to Mechanized.** Commission the survey link using the 2.2 output and register the result. The mast and terminal can be rebuilt, and a new automated batch can replace a lost module.

## 3. Mechanized

**Required inputs.** Electrical-tier controllers, copper and redstone, steel from iron and carbon stock, glass, processed insulation or polymers, standard drives, and basic energy cells.

| Tier | Machines, processes, and power | Unlocks and capability | Proof |
| --- | --- | --- | --- |
| 3.1 Precision | Machine tools, precision assembler, first materials lab, and medium-output power distribution. | Fabricate standardized bearings, frames, and controllers. Compare sample material properties through an in-game lab. | Use a precision assembly job to make a serviceable module. |
| 3.2 Surface Industry | Vehicle bay, charger or fuel service station, rail terminals, and robot dock. | Cars, trucks, trains, utility ships, tracked vehicles, tanks, industrial exoskeletons, and maintenance drones. Proposed fictional armament modules are optional. | Load, deliver, and service a cargo carrier at a second terminal. Use road, rail, or a water route. |
| 3.3 Industrial Control | Computers, servers, local digital storage, routing interfaces, and an industrial control network. | Schedule batches, monitor faults, dispatch utility robots, and share production status. | Bring the service route online and record the recovered relay data. |

**World and challenges.** A damaged industrial relay, hostile salvage constructs, and terrain obstacles test field servicing and cargo delivery. A marked low-risk route and baseline tools support the relay objective with affordable equipment. A heavy construct encounter can offer an optional vehicle module.

**Advance to Aerospace.** Register the commissioned service route and recover the relay record. Recover the record again from the relay terminal or its backup and repeat the service run after a vehicle loss.

## 4. Aerospace

**Required inputs.** Precision drives and controllers, light alloys, composites, pressure-rated glass, durable cells, and airframe stock. The first light-material and composite recipes run on the 3.1 lab and assembler or the entry-level Aerospace stations.

| Tier | Machines, processes, and power | Unlocks and capability | Proof |
| --- | --- | --- | --- |
| 4.1 Flight | Airframe bench, hangar, run-up diagnostics, and high-output charging or in-game fuel service. | Assemble propeller planes and fighter planes, cargo aircraft, and helicopters. Pilot, land, and service them. | Demonstrate a launch, landing, and turnaround at a hangar. |
| 4.2 Expedition Systems | Advanced airframe rig, pressure workshop, range relays, and depot storage. | Fighter jets, jetpacks, submarines, hovercraft, maglev routes, and expedition ship upgrades. A shared service interface handles field repairs and cargo. | Install expedition equipment and complete a depot resupply or beacon deployment. |
| 4.3 Remote Science | Mobile survey lab, remote beacon and tabletop survey projector. Use existing power, batteries, and relay links. | Collect high-altitude or submerged survey data and turn it into a local route display. | Restore the beacon and bring verified survey data home. |

**World and challenges.** A high-altitude ruin and a submerged observatory teach low visibility, recoverable weather faults, and expedition range. The beacon has a marked foot-access approach. Specialized vehicles expand access to optional sites.

**Advance to Atomic.** Register a successful 4.1 flight demonstration and return the 4.3 beacon data. The mandatory site can be reached with existing equipment before Atomic protection unlocks. Reissue beacon parts and use the checkpoint route after a failed trip.

## 5. Atomic

**Required inputs.** Steel and light alloy frames, shielding ceramics, protected conduits, refined composites, and invented power cartridges and cooling media produced with the age's entry containment station. Existing distributed power starts the age's machines.

| Tier | Machines, processes, and power | Unlocks and capability | Proof |
| --- | --- | --- | --- |
| 5.1 Containment | Shielded workstation, monitoring instruments, and a closed-loop model for in-game hazards. | Atomic sciences research, atomic-themed armor, protected transport modules, and remote inspection robots. | Verify safe access and containment status on a simulated test cell. |
| 5.2 Shielded Power | Fictional nuclear generator multiblock, buffered output, power isolation, and coolant status panels. | Run an independently protected industrial line. Unlock modular protection and early energy shields. Fictional nukes, missiles, and bombs are optional combat content for designated game challenges. | Supply a machine from the new generator and recover it from a modeled fault using its diagnostics. |
| 5.3 Remediation | Environmental purifiers, recovery chamber, and campus monitoring network. | Medical chambers, protective support robotics, and remediation of authored hazard zones. | Close the campus hazard objectives and verify its archive from a cleared sector. |

**World and challenges.** A contaminated research campus uses readable in-game hazard states and recovery points. Protective clothing or a remote service route allows access. Payloads are optional, permission-gated, and configurable by server operators. The planned mandatory route uses factory and remediation work.

**Advance to Fusion.** Validate the 5.2 shielded power demonstration and complete 5.3 campus recovery. Reset the campus instance or use its backup control terminal to reacquire evidence after failure.

## 6. Fusion

**Required inputs.** Advanced fabric, refined composites, specialty alloys, power cells, and fictional field modules assembled with Atomic-era shielding and drives. Prototype fusion work starts from an Atomic grid.

| Tier | Machines, processes, and power | Unlocks and capability | Proof |
| --- | --- | --- | --- |
| 6.1 High-Energy Industry | Fusion multiblock, high-energy fabricator, and particle physics instrument suite. | Fusion power, exotic material processing, supercomputers for research and scheduling, and precision field diagnostics. | Sustain a high-energy fabrication job and record verified field data. |
| 6.2 Adaptive Systems | Suit stations, android assembly, simulation-based cybernetics and biotechnology labs, and remote construction controller. | Modular powersuits, energy blades, stronger shields, pilotable mechs, androids, robotic companions, task-based artificial intelligence, and automated construction. Biotechnology produces fictional habitat and medical modules. | Assemble and service a protective or robotic system, then use the factory to resupply it. |
| 6.3 Overworld Mastery | Expedition fabrication bay, rescue beacons, and preflight supply staging. | Complete original boss encounters and certify closed resupply, recovery, and return for an expedition. | Clear the Overworld finale with available protection and support tools, then validate a resupply and rescue drill. |

**World and challenges.** An original boss chain at an Overworld research complex tests mobility, shielding, supply, and repair. Accessible field objectives support the team during the encounter. Players can re-enter from a recovery checkpoint. Optional harder encounters grant sidegrades.

**Advance to Orbital and Planetary.** Register the boss chain completion and expedition drill. This gate marks the end of the Overworld campaign. Its components are manufacturable on the ground, and the drills establish rescue access before any launch.

## 7. Orbital and Planetary

**Required inputs.** Fusion-era field modules, habitat composites, solar arrays, sealed storage, and reserves for expedition support. Regolith, asteroid ore, and anomalous samples enter production only after the corresponding destinations are available.

| Tier | Machines, processes, and power | Unlocks and capability | Proof |
| --- | --- | --- | --- |
| 7.1 Low Orbit | Launch pad, satellite integration, orbital fabricator, docks, solar arrays, and stored power. | Rockets, satellites, low-orbit routes, and first space stations. Establish scheduled cargo and a reliable return route. | Visit an orbital dock, deliver supplies, and return to the Overworld. |
| 7.2 Planetary Logistics | Shipyard, cargo relays, asteroid processing, and long-duration support systems. | Spaceships, asteroid mining, planetary landing, advanced remote robotics, and nanotech fabrication for modular nanotech suits. | Deliver off-world material to a station and complete return or rescue telemetry. |
| 7.3 Habitats | Colony modules, environmental controls, purification, greenhouse systems, and bounded terraforming equipment. | Planetary colonies, renewable local supplies, and terraforming of designated in-game zones. | Complete a colony resupply and analyze a returned anomaly sample. |

**World and challenges.** Derelict stations, asteroids, and planetary sites offer low gravity, shelter limits, and weather hazards. Return options and emergency rendezvous are unlocked before trips. Habitat milestones use measured delivery and environmental status. Optional destinations provide alternate extraction or transport choices.

**Advance to Cosmic Engineering.** Register the returned anomaly sample and a functioning off-world resupply. Recover an equivalent sample from a marked site or retry the cargo route after a loss. The first anomaly is accessible with 7.2 expedition equipment.

## 8. Cosmic Engineering

**Required inputs.** Orbital feedstock, exotic lattices, phase ceramics, and quantum modules derived from 7.3 samples with a Fusion-powered entry lab. Advanced dimensional materials become available after a return-capable route exists.

| Tier | Machines, processes, and power | Unlocks and capability | Proof |
| --- | --- | --- | --- |
| 8.1 Field Control | Quantum computers, holographic control systems, field bench, teleport nodes, and high-energy buffers. | Quantum armor, holograms, local teleportation, gravity manipulation, and matter replication limited by recipes, feedstock, and power. | Demonstrate a controlled local transfer and bring a test cargo back. |
| 8.2 Dimensional Industry | Gate facility, synchronized field control, and remote recovery anchors. | Dimensional travel, artificial wormholes for long-distance routes, and specialized survey vehicles or robots. | Complete a reversible dimensional journey with functional outbound and return endpoints. |
| 8.3 Stellar Megaprojects | Orbital assembly, high-throughput supply routing, and distributed stellar power collection. | Build modular Dyson sphere sectors as a community-scale project. Supply remote habitats and dimensional infrastructure from the network. | Commission one operational sector and deliver its output to a habitat or gate. |

**World and challenges.** Original dimensional anomalies, abandoned field arrays, and a late cooperative encounter test recovery and resilient supply. Optional sectors and destinations reward mastery. Replace failed modules and re-commission a sector to retry the capstone.

**Capstone.** A verified return from dimensional travel and an operational Dyson sphere sector complete the mainline. Further sectors, routes, and encounters extend play within Cosmic Engineering.
