package bg.sofia.uni.fmi.mjt.mss.threads;

import bg.sofia.uni.fmi.mjt.mss.music.Song;
import bg.sofia.uni.fmi.mjt.mss.server.MusicStreamingServer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static bg.sofia.uni.fmi.mjt.mss.Constants.REGEX;

public class MusicStreamingThread implements Runnable {
    private Socket socket;
    private String input;
    private MusicStreamingServer musicStreamingServer;

    @Override
    public void run() {
        String songName = input.split(REGEX, 2)[1];
        Song currentSong = null;
        for (Song song : musicStreamingServer.getSongBase()) {
            if (song.getName().equals(songName)) {
                currentSong = song;
            }
        }
        if (currentSong == null) {
            try {
                socket.getOutputStream().write("Nqma takava pesen, we\n".getBytes());
                socket.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(currentSong.getPathToSong()));
            AudioFormat audioFormat = audioInputStream.getFormat();
            String audioFormatString = audioFormat.getEncoding().toString() + " " + audioFormat.getSampleRate() + " "
                    + audioFormat.getSampleSizeInBits() + " "
                    + audioFormat.getChannels() + " " + audioFormat.getFrameSize() + " "
                    + audioFormat.getFrameRate() + " " + audioFormat.isBigEndian();
            OutputStream out = socket.getOutputStream();
            out.write("play\n".getBytes());
            out.flush();
            out.write(audioFormatString.getBytes());
            out.flush();

            int nBytesRead = 0;

            byte[] buffer = new byte[1024];

            while ((nBytesRead = audioInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, buffer.length);
                out.flush();
            }
            out.close();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MusicStreamingThread(Socket socket, String input, MusicStreamingServer musicStreamingServer) {
        this.socket = socket;
        this.input = input;
        this.musicStreamingServer = musicStreamingServer;
    }
}
