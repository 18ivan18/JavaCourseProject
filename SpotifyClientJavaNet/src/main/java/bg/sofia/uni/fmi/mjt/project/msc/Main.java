package bg.sofia.uni.fmi.mjt.project.msc;

import bg.sofia.uni.fmi.mjt.project.msc.client.MusicStreamingClient;

public class Main {
    public static void main(String[] args) {
        MusicStreamingClient musicStreamingClient = new MusicStreamingClient();
        musicStreamingClient.start();
    }
}
