package bg.sofia.uni.fmi.mjt.mss.user;

import bg.sofia.uni.fmi.mjt.mss.music.Playlist;
import bg.sofia.uni.fmi.mjt.mss.music.Song;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String username;
    private Set<Playlist> playlists;
    private Song currentlyPlaying;
    private short isPlaying;

    public User(String username) {
        this.username = username;
        playlists = new HashSet<>();
        currentlyPlaying = new Song("da", "da", "da");
        isPlaying = 0;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public Song getCurrentlyPlaying() {
        return currentlyPlaying;
    }
}
