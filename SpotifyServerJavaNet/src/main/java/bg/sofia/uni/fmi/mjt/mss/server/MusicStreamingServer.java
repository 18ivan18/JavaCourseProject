package bg.sofia.uni.fmi.mjt.mss.server;

import bg.sofia.uni.fmi.mjt.mss.checkers.FileTypeChecker;
import bg.sofia.uni.fmi.mjt.mss.commands.CommandsExecutor;
import bg.sofia.uni.fmi.mjt.mss.music.Song;
import bg.sofia.uni.fmi.mjt.mss.threads.ClientThread;
import bg.sofia.uni.fmi.mjt.mss.threads.MusicStreamingThread;
import bg.sofia.uni.fmi.mjt.mss.user.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static bg.sofia.uni.fmi.mjt.mss.Constants.INITIAL_DIRECTORY;

public class MusicStreamingServer {
    private ServerSocket serverSocket;
    private int port;
    private ExecutorService threadPool;
    private Map<String, String> registered;
    private Map<Socket, User> currentUsers;
    private CommandsExecutor commandsExecutor;
    private Set<Song> songBase;
    private boolean accept;

    public MusicStreamingServer(int port) {
        this.port = port;
    }

    public void start() {
        setup();
        acceptConnections();
    }

    private void acceptConnections() {
        Socket clientConnection;
        String input;
        while (true) {
            try {
                    clientConnection = serverSocket.accept();
                    input = normalThread(clientConnection);
                    if  (!input.contains("play")) {
                        ClientThread clientThread = new ClientThread(clientConnection, this);
                        threadPool.execute(clientThread);
                    } else {
                        MusicStreamingThread musicStreamingThread = new MusicStreamingThread(clientConnection, input, this);
                        threadPool.execute(musicStreamingThread);
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String normalThread(Socket clientConnection) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
        return input.readLine();
    }

    public Map<String, String> getRegistered() {
        return registered;
    }

    public Map<Socket, User> getCurrentUsers() {
        return currentUsers;
    }

    public Set<Song> getSongBase() {
        return songBase;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public CommandsExecutor getCommandsExecutor() {
        return commandsExecutor;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    private void setup() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        accept = true;
        threadPool = Executors.newFixedThreadPool(10);
        registered = new HashMap<>();
        currentUsers = new HashMap<>();
        commandsExecutor = new CommandsExecutor(this);
        songBase = new HashSet<>();
        loadSongs(INITIAL_DIRECTORY);
    }

    private void loadSongs(String directory) {
        File initialDir = new File(directory);
        File[] directoryListing = initialDir.listFiles();
        for (File file : directoryListing) {
            if (FileTypeChecker.isWavFile(file)) {
                String[] words = file.getName().split("[-.]");
                songBase.add(new Song(words[1], words[0], directory + "\\" + file.getName()));
            }
            else if (file.isDirectory()) {
                loadSongs(directory + "\\" + file.getName());
            }
        }
    }
}
