package com.edasaki.rensa.casino.commands;

import java.util.List;
import java.util.Map.Entry;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.casino.CasinoManager;
import com.edasaki.rensa.commands.CommandMatchType;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class LeaderboardCommand extends AbstractCommand {

    public LeaderboardCommand(String identifier, CommandMatchType type) {
        super(identifier, type);
    }

    @Override
    protected void execute(String[] args, IMessage msg) {
        List<Entry<String, Integer>> top10 = CasinoManager.getLeaderboard();
        StringBuilder sb = new StringBuilder();
        sb.append("```diff\n");
        sb.append("+ Casino Leaderboard +\n");
        int k = 1;
        for (Entry<String, Integer> e : top10) {
            sb.append(k++);
            sb.append(". ");
            IUser user = msg.getGuild().getUserByID(e.getKey());
            if (user != null)
                sb.append(user.getDisplayName(msg.getGuild()));
            else
                sb.append("Unknown User");
            sb.append(" - ");
            sb.append(e.getValue());
            sb.append(' ');
            sb.append(CasinoManager.CURRENCY_NAME);
            sb.append('\n');
        }
        sb.append("```");
        Rensa.getInstance().sendMessage(msg.getChannel(), sb.toString());
    }

    @Override
    public String getDescription() {
        return "View the top 10 richest users.";
    }

}
