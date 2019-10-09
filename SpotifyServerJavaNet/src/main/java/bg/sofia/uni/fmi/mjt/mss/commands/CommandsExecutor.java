package bg.sofia.uni.fmi.mjt.mss.commands;

import bg.sofia.uni.fmi.mjt.mss.interfaces.Command;
import bg.sofia.uni.fmi.mjt.mss.server.MusicStreamingServer;

import java.net.Socket;
import java.util.List;

public class CommandsExecutor {
    private List<Command> commands;
    private MusicStreamingServer musicStreamingServer;

    public CommandsExecutor(MusicStreamingServer musicStreamingServer) {
        this.musicStreamingServer = musicStreamingServer;
        commands = List.of(new Register(musicStreamingServer), new Login(musicStreamingServer),
                new Search(musicStreamingServer));
    }

    public String executeCommand(String input, Socket socket) {
        String commandFirstWord = input.contains(" ") ? input.split(" ")[0] : input;
        String output = null;
        for (Command command : commands) {
            if (command.toString().equals(commandFirstWord)) {
                output = command.execute(input, socket);
                break;
            }
        }
        return output;
    }

}
