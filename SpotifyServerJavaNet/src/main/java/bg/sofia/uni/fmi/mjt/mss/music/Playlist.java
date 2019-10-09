package bg.sofia.uni.fmi.mjt.mss.music;

import java.util.HashSet;
import java.util.Set;

public class Playlist {
    String playlistName;
    private Set<Song> songSet;

    public Playlist(String playlistName) {
        this.songSet = new HashSet<>();
        this.playlistName = playlistName;
    }

    public String getName() {
        return playlistName;
    }

    public Set<Song> getSongs() {
        return songSet;
    }

    public void addSong(Song song) {
        songSet.add(song);
    }
}
