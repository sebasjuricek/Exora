# The first shop

Workshop takes a player from a stone mallet to a little production line they can leave running. All three proofs can be earned at home in the Overworld. The only ingredients from underground are ordinary furnace stone, iron and copper. Trees supply both construction timber and the generator's charcoal.

## From an empty workshop to stored power

1. **Set up a work surface.** Make a crafting table and furnace, then smelt one cobblestone into stone. On the crafting table place three planks across the top row and cobblestone, stone, cobblestone beneath them. For the Stone Mallet, place two cobblestone side by side with a stick under either end. Both plans appear in the vanilla recipe book as you gather materials.
2. **Make something the table can't.** Open the Workshop Bench with the reusable mallet in your pack. Choose Timber Frame and press **Assemble**. Two planks and two sticks become the frame that opens the Hand Press plan. The ledger shows your chapter, completed proofs, quantities in your pack and the next job. Use the chapter tabs to revisit the instructions and the arrows or wheel to browse plans.
3. **Set up the metal shop.** Turn that frame, two cobblestone and two sticks into a Hand Press. Open the press, choose a die and work the lever. Two iron ingots stamp four Iron Plates. One ingot turns two Iron Shafts. Back at the bench, assemble two shafts and a plate into a Drive Assembly. Choose **Fit drive** at the press. Its flywheel appears, the ram returns faster, and the new **×4** control processes four batches in a single stroke. A second press can still serve as a hand-operated backup. Mining a fitted press returns the drive.
4. **Put the shop to work.** Make additional frames and plate them into Machine Casings. Wind copper at the bench, two Copper Windings per ingot. Assemble the Starter Generator and Coil Mill from the plans below. Optional lanterns light the work floor. A hopper can later feed the line. For a portable reading of stored energy, use a Line Meter on a machine face and use it again to see the change.
5. **Commission the line.** Place the generator and mill touching on any face. Put a Copper Winding in the mill's input and charcoal in the generator. The workpiece takes four seconds at normal tick speed. Watch the mill change from waiting for power to running, then **take the two Wound Cores from the output slot yourself**. An eligible team member can take them. The completed generator-powered job opens Electrical and the bench records the third proof. Keep an extraction hopper off this first output, or run a replacement winding and take that batch by hand.
6. **Run on reserve.** Use both cores, a casing and a glass block to build the Energy Buffer. Set the generator, buffer and mill in a line with the buffer in the middle. Let it store at least 192 FE, then remove the generator with a pickaxe. Give the mill an iron ingot. The buffer runs the job and the tray holds three plates. The generator can now be used elsewhere.

For the required parts through the first buffer, budget **7 iron ingots and 2 copper ingots**, plus glass, charcoal, timber and cobblestone. That includes two drives, three casings, the press and both machines. Bring an eighth iron ingot for the stored-power plate run. A furnace goes into the generator. Keeping a second furnace on the shop floor makes it easier to replenish charcoal.

## Plans and throughput

Keep the mallet in your pack or off hand for every bench plan. The bench consumes ingredients from your main inventory. Each press operation uses the installed lever. The output goes into your pack or drops beside you if the pack is full.

| Station | Input | Output or change | Available after |
| --- | --- | --- | --- |
| Bench | 2 planks, 2 sticks | Timber Frame | Start |
| Bench | Timber Frame, 2 cobblestone, 2 sticks | Hand Press | Frame proof |
| Hand Press | 2 iron ingots | 4 Iron Plates | Frame proof |
| Hand Press | 1 iron ingot | 2 Iron Shafts | Frame proof |
| Bench | 1 copper ingot | 2 Copper Windings | Frame proof |
| Bench | 2 Iron Shafts, Iron Plate | Drive Assembly | Frame proof |
| Bench | Timber Frame, 2 Iron Plates | Machine Casing | Frame proof |
| Bench | Iron Plate, 2 torches | 2 lanterns | Frame proof |
| Bench | Timber Frame, 2 Iron Plates | Hopper, an alternative use for the same stock as a casing | Frame proof |
| Hand Press | Drive Assembly | Installed drive and four-batch strokes | Frame proof |
| Bench | Machine Casing, Copper Winding, Iron Shaft, furnace | Starter Generator | Fitted drive |
| Bench | Machine Casing, Copper Winding, Drive Assembly | Coil Mill | Fitted drive |
| Bench | Iron Plate, Copper Winding, glass | Line Meter | Fitted drive |
| Coil Mill | Copper Winding + 320 FE | 2 Wound Cores in 80 ticks | Working powered line |
| Coil Mill | Iron ingot + 192 FE | 3 Iron Plates in 48 ticks | Working powered line |
| Coil Mill | Copper ingot + 224 FE | 4 Copper Windings in 56 ticks | Working powered line |
| Bench | Machine Casing, 2 Wound Cores, glass | Energy Buffer | Commissioning proof |

## Reading the equipment

The Starter Generator accepts charcoal. One piece supplies 2,560 FE over 320 working ticks. Its governor pauses the burn when its 1,024 FE reservoir is full. The Coil Mill draws 4 FE per working tick and holds at most 48 FE internally. Even the shortest default job needs an external supply. The 4,096 FE buffer supplies adjacent receivers at up to 16 FE per tick.

| What you see | What to do |
| --- | --- |
| Waiting for stock or an unrecognized material | Open the mill's recipe list. Insert a supported input or remove the unrecognized stack. |
| Workpiece present, no power | Supply charcoal and check that the machines share a face. The mill keeps the workpiece and its progress. |
| Output blocked | Collect the completed batch. The next input stays in the feed until the tray clears. |
| The cores were extracted automatically | Feed another winding while touching a fueled Starter Generator. Collect the new output by hand. |
| The bench still shows an earlier proof | Check the team shown by the vanilla scoreboard. A new operator can repeat the frame, fit another drive, or commission another batch. |

The mill keeps one workpiece, its expected result and its elapsed time through a normal save or unload. Breaking the mill returns the unprocessed workpiece together with inventory contents. The buffered power and fuel reserve survive a normal save. Automate established jobs with a hopper feeding the mill from above or a side and a hopper collecting underneath. A hopper can also feed charcoal into the generator.

## Shared ownership

An unteamed player has a personal ledger keyed by their UUID. A vanilla scoreboard team has one ledger keyed by its internal team name across dimensions. An operator can create a team with `/team add workshop` and add players using `/team join workshop <player>`. Join before the first proof to build one shop together. One member can make the frame, another can fit the drive, and either can collect the commissioning output. Every online teammate sees the notice, and offline members see the result when they return to the bench.

Changing teams selects that team's ledger. Leaving selects your prior personal ledger. Rejoining restores access to the team's work. Reusing a deleted team's internal name reopens its saved ledger. Use a new team name to start another shop from scratch. The ledger lives in the Overworld's `data/exora_workshop.dat`. Stations are shared physical machines, so use a separate press or refit a recovered drive if another team has already geared the only press.
