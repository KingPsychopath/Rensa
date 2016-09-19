package com.edasaki.rensa.music;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractManager;
import com.edasaki.rensa.config.ConfigManager;
import com.edasaki.rensa.exceptions.InvalidChannelIDException;
import com.edasaki.rensa.logging.Saki;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.audio.AudioPlayer;
import sx.blah.discord.util.audio.AudioPlayer.Track;
import sx.blah.discord.util.audio.events.TrackStartEvent;

@SuppressWarnings("unused")
public class MusicManager extends AbstractManager {

    public static MusicManager instance;

    private AudioPlayer player;

    private ArrayList<File> playlist = new ArrayList<File>();

    @Override
    public void initialize() {
        MusicManager.instance = this;
        String id = ConfigManager.getMusicChannel();
        if (id.length() > 0) {
            IVoiceChannel vc = Rensa.getInstance().client.getVoiceChannelByID(id);
            //            IVoiceChannel vc = Rensa.getInstance().client.getVoiceChannelByID("146395461945655296");
            if (vc == null) {
                try {
                    throw new InvalidChannelIDException(id);
                } catch (InvalidChannelIDException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    vc.join();
                    Saki.db("Joined Music channel.");
                    player = AudioPlayer.getAudioPlayerForGuild(vc.getGuild());
                    player.setVolume(0.3f);
                    setMusicFolderPlaylist(); // use music folder by default
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setPlaylist() {
        File[] files = Rensa.getDataFolder("playlists").listFiles();
        File rand = files[(int) (Math.random() * files.length)];
        setPlaylist(rand.getName());
    }

    public void setMusicFolderPlaylist() {
        playlist.clear();
        Saki.log("Switching to music folder playlist");
        File dir = Rensa.getDataFolder("music");
        if (!dir.exists())
            dir.mkdirs();
        for (File f : dir.listFiles()) {
            playlist.add(f);
        }
        updatePlayer();
    }

    public void setPlaylist(String name) {
        playlist.clear();
        Saki.log("Playing playlist: " + name);
        ArrayList<String> tempplaylist = new ArrayList<String>();
        File f = new File(Rensa.getDataFolder("playlists"), name);
        try (Scanner scan = new Scanner(f, "UTF-8")) {
            while (scan.hasNextLine()) {
                tempplaylist.add(scan.nextLine().trim());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (String s : tempplaylist) {
            if (!(new File(s).exists())) {
                Saki.error("Missing from playlist " + name + " song: " + s);
            } else {
                playlist.add(new File(s));
            }
        }
        updatePlayer();
    }

    @EventSubscriber
    public void onTrackStart(TrackStartEvent event) {
        Track t = event.getTrack();
        File f = (File) t.getMetadata().getOrDefault("file", null);
        String name = "unknown";
        if (f != null)
            name = f.getName();
        else {
            URL url = (URL) t.getMetadata().getOrDefault("url", null);
            if (url != null)
                name = url.getQuery();
        }
        for (IVoiceChannel channel : event.getClient().getConnectedVoiceChannels()) {
            if (channel.getUsersHere().size() > 1) {
                Rensa.getInstance().sendMessage(channel.getGuild(), "music", "Now playing: " + name);
            }
        }
    }

    public void forceSong(File f) {
        playlist.clear();
        playlist.add(f);
        updatePlayer();
    }

    private void updatePlayer() {
        player.setPaused(true);
        player.clear();
        player.setLoop(true);
        player.setPaused(false);
        Collections.shuffle(playlist);
        for (File f : playlist) {
            try {
                player.queue(f);
            } catch (IOException | UnsupportedAudioFileException e) {
                Saki.error("Error queueing " + f.getAbsolutePath());
                //                e.printStackTrace();
            }
        }
    }

}
