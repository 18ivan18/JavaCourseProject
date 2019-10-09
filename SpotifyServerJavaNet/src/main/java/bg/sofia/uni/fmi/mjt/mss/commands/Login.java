package bg.sofia.uni.fmi.mjt.mss.commands;

import bg.sofia.uni.fmi.mjt.mss.interfaces.Command;
import bg.sofia.uni.fmi.mjt.mss.server.MusicStreamingServer;
import bg.sofia.uni.fmi.mjt.mss.user.User;

import java.net.Socket;

import static bg.sofia.uni.fmi.mjt.mss.Constants.LOGIN;
import static bg.sofia.uni.fmi.mjt.mss.Constants.REGEX;

public class Login extends Command {
    public Login(MusicStreamingServer musicStreamingServer) {
        super(musicStreamingServer);
    }

    @Override
    public String execute(String input, Socket socket) {
        if (musicStreamingServer.getCurrentUsers().keySet().contains(socket)) {
            return "Already logged in";
        }
        String[] words = input.split(REGEX);
        String username = words[1];
        if (!musicStreamingServer.getRegistered().containsKey(username)) {
            return "Account doesn't exist";
        }
        String password = words[2];
        if (!musicStreamingServer.getRegistered().get(username).equals(password)) {
            return "Wrong password";
        }
        musicStreamingServer.getCurrentUsers().put(socket, new User(username));
        return "Logged in successfully";
    }

    @Override
    public String toString() {
        return LOGIN;
    }
}
