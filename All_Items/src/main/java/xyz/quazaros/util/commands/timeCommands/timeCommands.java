package xyz.quazaros.util.commands.timeCommands;

import org.bukkit.entity.Player;
import xyz.quazaros.main;
import xyz.quazaros.util.main.mainVariables;

public class timeCommands {
    static public void timeStart(Player p) {
        mainVariables Main = main.getPlugin().variables;

        if (!p.isOp()) {
            p.sendMessage(Main.lang.colorBad + Main.lang.noPermission);
            return;
        }

        Main.timer.startTimer();

        p.sendMessage(Main.lang.colorGood + Main.lang.timerStart);
    }

    static public void timePause(Player p) {
        mainVariables Main = main.getPlugin().variables;

        if (!p.isOp()) {
            p.sendMessage(Main.lang.colorBad + Main.lang.noPermission);
            return;
        }

        Main.timer.pauseTimer();

        p.sendMessage(Main.lang.colorGood + Main.lang.timerPause);
    }

    static public void timeStop(Player p) {
        mainVariables Main = main.getPlugin().variables;

        if (!p.isOp()) {
            p.sendMessage(Main.lang.colorBad + Main.lang.noPermission);
            return;
        }

        Main.timer.stopTimer();

        p.sendMessage(Main.lang.colorGood + Main.lang.timerStop);
    }

    static public void timeReset(Player p) {
        mainVariables Main = main.getPlugin().variables;

        if (!p.isOp()) {
            p.sendMessage(Main.lang.colorBad + Main.lang.noPermission);
            return;
        }

        Main.timer.resetTimer();

        p.sendMessage(Main.lang.colorGood + Main.lang.timerReset);
    }

    static public void timeSet(Player p, String[] args) {
        mainVariables Main = main.getPlugin().variables;

        if (!p.isOp()) {
            p.sendMessage(Main.lang.colorBad + Main.lang.noPermission);
            return;
        }

        if (args.length < 1) {
            p.sendMessage(Main.lang.colorBad + Main.lang.timerNumError);
            return;
        }

        Main.timer.setTimer(Integer.parseInt(args[0]));

        p.sendMessage(Main.lang.colorDom + Main.lang.timerSet + ": " + Main.lang.colorSec + args[0]);
    }

    static public void timeGet(Player p) {
        mainVariables Main = main.getPlugin().variables;

        int curTime = Main.timer.getTimer();

        p.sendMessage(Main.lang.colorDom + Main.lang.timerGet + ": " + Main.lang.colorSec + curTime);
    }

    static public void timeActive(Player p) {
        mainVariables Main = main.getPlugin().variables;

        boolean isActive = Main.timer.getActive();
        String suffix = isActive ? Main.lang.timerTrue : Main.lang.timerFalse;

        p.sendMessage(Main.lang.colorDom + Main.lang.timerActive + ": " + Main.lang.colorSec + suffix);
    }
}
