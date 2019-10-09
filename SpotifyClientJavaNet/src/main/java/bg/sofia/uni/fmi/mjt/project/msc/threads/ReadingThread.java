package bg.sofia.uni.fmi.mjt.project.msc.threads;

import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static bg.sofia.uni.fmi.mjt.project.msc.Constants.HOSTNAME;
import static bg.sofia.uni.fmi.mjt.project.msc.Constants.PORT;
import static bg.sofia.uni.fmi.mjt.project.msc.Constants.REGEX;

public class ReadingThread implements Runnable {
    private BufferedReader input;
    private Socket socket;

    public void run() {
        setup();
        String response;
        try {
            while ((response = input.readLine()) != null) {
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setup() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playMusic(String audioFormatString) {
        String[] elements = audioFormatString.split(REGEX);
        AudioFormat.Encoding encoding = new AudioFormat.Encoding(elements[0]);
        float sampleRate = Float.parseFloat(elements[1]);
        int sampleSizeInBits = Integer.parseInt(elements[2]);
        int channels = Integer.parseInt(elements[3]);
        int frameSize = Integer.parseInt(elements[4]);
        float frameRate = Float.parseFloat(elements[5]);
        boolean bigEndian = Boolean.parseBoolean(elements[6]);
        AudioFormat audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels,
                frameSize, frameRate, bigEndian);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

        SourceDataLine dataLine = null;
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open();
            dataLine.start();

            int nBytesRead = 0;
            byte[] buffer = new byte[1024];

            Socket musicSocket = new Socket(HOSTNAME, PORT);
            InputStream inputStream = musicSocket.getInputStream();
            while ((nBytesRead = inputStream.read(buffer)) != -1) {
                dataLine.write(buffer, 0, nBytesRead);
            }
            System.out.println("Finished playing song");

            dataLine.drain();
            dataLine.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReadingThread(Socket socket) {
        this.socket = socket;
    }
}
