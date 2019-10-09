package bg.sofia.uni.fmi.mjt.project.msc.client;

import bg.sofia.uni.fmi.mjt.project.msc.threads.ReadingThread;
import bg.sofia.uni.fmi.mjt.project.msc.threads.SingingThread;
import bg.sofia.uni.fmi.mjt.project.msc.validators.InputValidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static bg.sofia.uni.fmi.mjt.project.msc.Constants.HOSTNAME;
import static bg.sofia.uni.fmi.mjt.project.msc.Constants.PORT;

public class MusicStreamingClient {
    private Socket connection;
    private Scanner scanner;
    private PrintWriter output;
    private SingingThread singingThread;


    public void start() {
        try {
            setup();
            sendRequests();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequests() {
        String line;
        while (true) {
            line = scanner.nextLine();
            if (InputValidator.validatePlay(line)) {
                singingThread = new SingingThread(line);
                new Thread(singingThread).start();
                continue;
            }
            if (InputValidator.validate(line)) {
                output.println(line);
                continue;
            }

            if (InputValidator.validatePause(line)) {
                singingThread.setPlaying(false);
                continue;
            }

            if (InputValidator.validateContinue(line)) {
                synchronized (singingThread) {
                    singingThread.setPlaying(true);
                    singingThread.notify();
                    continue;
                }
            }

            if (InputValidator.validateDisconnect(line)) {
                break;
            }
            System.out.println("Enter a valid command");
        }
    }

    private void setup() throws IOException {
        connection = new Socket(HOSTNAME, PORT);
        connection.getOutputStream().write("ne e za puskane na muzika\n".getBytes());
        connection.getOutputStream().flush();
        ReadingThread readingThread = new ReadingThread(connection);
        new Thread(readingThread).start();
        scanner = new Scanner(System.in);
        output = new PrintWriter(connection.getOutputStream(), true);
    }
}
