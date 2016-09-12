package com.edasaki.rensa.commands.misc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.commands.CommandMatchType;
import com.edasaki.rensa.logging.Saki;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import sx.blah.discord.handle.obj.IMessage;

public class StatusCommand extends AbstractCommand {
    private static final String ERROR_MESSAGE = "I was unable to retrieve the server status. Sorry!";

    public StatusCommand(String identifier, CommandMatchType type) {
        super(identifier, type);
    }

    @Override
    protected void execute(String[] args, IMessage msg) {
        try {
            URL url = new URL(" https://mcapi.ca/query/play.zentrela.net/info");
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            JsonElement element = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject root = element.getAsJsonObject();
            if (!root.get("status").getAsBoolean()) {
                Saki.error("Status from API was " + root.get("status").getAsBoolean());
                Rensa.getInstance().sendMessage(msg.getChannel(), ERROR_MESSAGE);
                return;
            }
            JsonObject playerArr = root.getAsJsonObject("players");
            int currPlayers = playerArr.get("online").getAsInt();
            int maxPlayers = playerArr.get("max").getAsInt();
            String motd = root.get("motd").getAsString(); //just grab the zipcode
            StringBuilder sb = new StringBuilder();
            sb.append("```diff\n");
            sb.append("! Zentrela Server Status\n");
            sb.append("+ Players Online\n");
            sb.append("~ " + currPlayers + "/" + maxPlayers + "\n");
            sb.append("+ MOTD\n");
            for (String tempMotd : motd.split("\n")) {
                sb.append("~ " + tempMotd + "\n");
            }
            sb.append("- play.zentrela.net\n");
            sb.append("```");
            Rensa.getInstance().sendMessage(msg.getChannel(), sb.toString());
            return;
        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
        Rensa.getInstance().sendMessage(msg.getChannel(), ERROR_MESSAGE);
    }

}
