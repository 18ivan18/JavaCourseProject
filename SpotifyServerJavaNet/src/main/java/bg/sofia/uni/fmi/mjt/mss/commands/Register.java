package bg.sofia.uni.fmi.mjt.mss.commands;

import bg.sofia.uni.fmi.mjt.mss.interfaces.Command;
import bg.sofia.uni.fmi.mjt.mss.server.MusicStreamingServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static bg.sofia.uni.fmi.mjt.mss.Constants.REGEX;
import static bg.sofia.uni.fmi.mjt.mss.Constants.REGISTER;

public class Register extends Command {
    public Register(MusicStreamingServer musicStreamingServer) {
        super(musicStreamingServer);
    }

    public String execute(String input, Socket clientSocket) {
        String[] words = input.split(REGEX);
        String username = words[1];
        if (musicStreamingServer.getRegistered().containsKey(username)) {
            return "Username already in use";
        }
        String password = words[2];
        musicStreamingServer.getRegistered().put(username, password);
        return "Registration successful";
    }

    @Override
    public String toString() {
        return REGISTER;
    }
}
