package bg.sofia.uni.fmi.mjt.mss.commands;

import bg.sofia.uni.fmi.mjt.mss.interfaces.Command;
import bg.sofia.uni.fmi.mjt.mss.music.Song;
import bg.sofia.uni.fmi.mjt.mss.server.MusicStreamingServer;

import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.mss.Constants.REGEX;
import static bg.sofia.uni.fmi.mjt.mss.Constants.SEARCH;

public class Search extends Command {
    public Search(MusicStreamingServer musicStreamingServer) {
        super(musicStreamingServer);
    }

    @Override
    public String execute(String input, Socket socket) {
        //TODO check if logged in
        String pattern = input.split(REGEX, 2)[1];
        List<Song> songs = musicStreamingServer.getSongBase().stream().filter(e -> e.getName().contains(pattern)
                || e.getAuthor().contains(pattern)).collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        songs.forEach(e -> stringBuilder.append(e.toString() + System.lineSeparator()));
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return SEARCH;
    }
}
