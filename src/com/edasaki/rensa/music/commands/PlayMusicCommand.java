package com.edasaki.rensa.music.commands;

import java.io.File;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.commands.CommandMatchType;
import com.edasaki.rensa.music.MusicManager;

import sx.blah.discord.handle.obj.IMessage;

public class PlayMusicCommand extends AbstractCommand {

    public PlayMusicCommand(String identifier, CommandMatchType type) {
        super(identifier, type);
    }

    @Override
    protected void execute(String[] args, IMessage msg) {
        if (!msg.getAuthor().getID().equals("81017579937738752")) {
            Rensa.getInstance().sendMessage(msg.getChannel(), "Music playing is currently incomplete and can only be used by Misaka.");
        } else {
            if (args[0].equalsIgnoreCase("folder")) {
                MusicManager.instance.setMusicFolderPlaylist();
                Rensa.getInstance().sendMessage(msg.getChannel(), "Changed playlist to music folder.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < args.length; k++) {
                    sb.append(args[k]);
                    sb.append(' ');
                }
                String name = sb.toString().trim();
                if (new File(name).exists()) {
                    MusicManager.instance.forceSong(new File(name));
                    Rensa.getInstance().sendMessage(msg.getChannel(), "Forced song: " + name);
                } else if (new File(Rensa.getDataFolder("playlists"), name).exists()) {
                    MusicManager.instance.setPlaylist(name);
                    Rensa.getInstance().sendMessage(msg.getChannel(), "Changed playlist to: " + name);
                } else {
                    Rensa.getInstance().sendMessage(msg.getChannel(), "Unknown syntax, or file not found.");
                }

            }
        }
    }

    @Override
    public String getDescription() {
        return "Play music. Currently incomplete.";
    }

}
