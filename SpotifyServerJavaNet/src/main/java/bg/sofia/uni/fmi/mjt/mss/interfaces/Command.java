package bg.sofia.uni.fmi.mjt.mss.interfaces;

import bg.sofia.uni.fmi.mjt.mss.server.MusicStreamingServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

abstract public class Command {
    protected MusicStreamingServer musicStreamingServer;

    abstract public String execute(String input, Socket socket);

    public Command(MusicStreamingServer musicStreamingServer) {
        this.musicStreamingServer = musicStreamingServer;
    }

}

