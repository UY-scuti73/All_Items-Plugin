package xyz.quazaros.extra.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.quazaros.main;

import java.util.concurrent.ScheduledExecutorService;

public class timer {
    public boolean timerActive;
    public int timerTime;

    private BukkitTask timerTask;

    ScheduledExecutorService scheduler;

    public timer() {
        timerActive = false;
        timerTime = 0;
    }

    //Starts Timer When Server Starts If Applicable
    public void startUp() {
        startTimer();
    }

    //Starts Timer
    public void startTimer() {
        timerActive = true;

        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                timerTime++;
            }
        }.runTaskTimer(main.getPlugin(), 0L, 20L);
    }

    //Pauses Timer
    public void pauseTimer() {
        cancel();
        timerActive = false;
    }

    //Stops Timer & Resets To 0
    public void stopTimer() {
        cancel();
        timerTime = 0;
        timerActive = false;
    }

    //Resets Timer
    public void resetTimer() {
        stopTimer();
        startTimer();
    }

    //Cancel Timer Task
    private void cancel() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    //Sets Timer Value
    public void setTimer(int val) {
        stopTimer();
        timerTime = val;
        startTimer();
    }

    //Gets Timer Value
    public int getTimer() {
        return timerTime;
    }

    //Returns if the timer is active or not
    public boolean getActive() {
        return timerActive;
    }
}
