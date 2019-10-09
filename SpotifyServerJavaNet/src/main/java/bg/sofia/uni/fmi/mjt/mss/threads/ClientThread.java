package bg.sofia.uni.fmi.mjt.mss.threads;

import bg.sofia.uni.fmi.mjt.mss.server.MusicStreamingServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket clientSocket;
    private MusicStreamingServer musicStreamingServer;
    private BufferedReader input;
    private PrintWriter output;

    public void run() {
        setup();
        executeCommand();
    }

    private void executeCommand() {
        String line = null;
        try {
            while ((line = input.readLine()) != null) {
                String response = musicStreamingServer.getCommandsExecutor().executeCommand(line, clientSocket);
                output.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setup() {
        try {
            this.input =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientThread(Socket clientSocket, MusicStreamingServer musicStreamingServer) {
        this.clientSocket = clientSocket;
        this.musicStreamingServer = musicStreamingServer;
    }
}
