package bg.sofia.uni.fmi.mjt.mss;

import bg.sofia.uni.fmi.mjt.mss.server.MusicStreamingServer;

import static bg.sofia.uni.fmi.mjt.mss.Constants.PORT;

public class Main {
    public static void main(String[] args) {
        MusicStreamingServer musicStreamingServer = new MusicStreamingServer(PORT);
        musicStreamingServer.start();
    }
}
