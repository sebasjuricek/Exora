# Two-client Workshop test

These are inputs for the opt-in development driver. Use a new local test world and the three Gradle runs in CONTRIBUTING.md. The server commands lay out a platform at Y=160, a log and a supply chest. Wait for both players to join before applying the team and teleport commands. Queue each named step file by copying it to the corresponding client's run directory as `workshop.steps`. Wait for the end marker in that client's log before queuing another file.

1. Fitter runs `01-gather`. Move Fitter to `6.5 161 6.5` before running `02-bench` and `03-metalwork` so their body clears the furnace and other placement tiles. Winder can open the same bench to watch progression update.
2. Save and restart the server. Reconnect the clients. Fitter runs `04-commission`. Look for the mill waiting for power. Leave the queued winding in the input.
3. Freeze the disposable server with `tick freeze`. On Fitter, close the mill, open the generator at `4 161 5` and shift-click the charcoal into its fuel slot. Run `tick step 25`. Inspect `data get block 4 161 6` for an active workpiece, then `save-all flush` and `stop`. Restart the server and reconnect both clients.
4. Position Winder at `5.6 161 7.5` and run `05-collect-winder`. The blocked tray holds the first two cores. Winder collects them and leaves them in the chest for Fitter. Fitter runs `06-buffer-fitter` to use those cores and charge the buffer.
5. Position Fitter at `3.55 161 6.1` so harvested machine drops land in reach. Run `07-island-fitter`. The original generator ends in Fitter's inventory. The relocated buffer runs the plate job.
6. Stop and restart the server again. Open the bench on both clients to verify three proofs and the Electrical plans. Check a personal ledger by leaving the team, then rejoin to recover access to the shared ledger.

The driver reads inputs only while connected to the local test server. Screenshots are written to each client's disposable screenshots directory.
