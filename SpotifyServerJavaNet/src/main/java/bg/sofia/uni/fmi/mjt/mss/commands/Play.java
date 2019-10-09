package bg.sofia.uni.fmi.mjt.mss.commands;

import bg.sofia.uni.fmi.mjt.mss.interfaces.Command;
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

import static bg.sofia.uni.fmi.mjt.mss.Constants.PLAY;
import static bg.sofia.uni.fmi.mjt.mss.Constants.REGEX;

public class Play extends Command {
    public Play(MusicStreamingServer musicStreamingServer) {
        super(musicStreamingServer);
    }

    @Override
    public String execute(String input, Socket socket) {
        String songName = input.split(REGEX, 2)[1];
        Song currentSong = null;
        for (Song song : musicStreamingServer.getSongBase()) {
            if (song.getName().equals(songName)) {
                currentSong = song;
            }
        }

        if (currentSong == null) {
            return "No such song found";
        }

        OutputStream out = null;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(currentSong.getPathToSong()));
            AudioFormat audioFormat = audioInputStream.getFormat();
            String audioFormatString = audioFormat.getEncoding().toString() + " " + audioFormat.getSampleRate() + " "
                    + audioFormat.getSampleSizeInBits() + " "
                    + audioFormat.getChannels() + " " + audioFormat.getFrameSize() + " "
                    + audioFormat.getFrameRate() + " " + audioFormat.isBigEndian() + "\n";
                out = socket.getOutputStream();
            out.write("play\n".getBytes());
            out.flush();
            out.write(audioFormatString.getBytes());
            out.flush();


        byte[] buffer = new byte[1024];
        int count;
        int total = 0;
        Socket musicSocket = musicStreamingServer.getServerSocket().accept();
        out = musicSocket.getOutputStream();
        while ((count = audioInputStream.read(buffer)) != -1) {
            total += count;
            out.write(buffer, 0, buffer.length);
            out.flush();
        }
        //out.write("end".getBytes());
        //out.flush();
            out.close();
        //System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        return "Song Streamed correctly";
    }

    @Override
    public String toString() {
        return PLAY;
    }
}
