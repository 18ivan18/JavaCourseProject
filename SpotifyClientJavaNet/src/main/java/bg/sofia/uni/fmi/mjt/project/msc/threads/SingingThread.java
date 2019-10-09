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

public class SingingThread implements Runnable {
    private String input;
    private boolean playing;


    public SingingThread(String input) {
        this.input = input;
        playing = true;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    @Override
    public void run() {
        try {
            Socket singingSocket = new Socket(HOSTNAME, PORT);
            singingSocket.getOutputStream().write((input + "\n").getBytes());

            BufferedReader input = new BufferedReader(new InputStreamReader(singingSocket.getInputStream()));

            if (input.readLine().equals("Nqma takava pesen, we")) {
                System.out.println("Nqma takava pesen, we");
                return;
            }

            String audioFormatString = input.readLine();

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
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open();
            dataLine.start();
            synchronized (this) {
                int nBytesRead = 0;
                byte[] buffer = new byte[1024];

                InputStream inputStream = singingSocket.getInputStream();
                while ((nBytesRead = inputStream.read(buffer)) != -1) {
                    while (!playing) {
                        wait();
                    }
                    dataLine.write(buffer, 0, nBytesRead);
                }
                System.out.println("Finished playing song");
            }
            dataLine.drain();
            dataLine.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
