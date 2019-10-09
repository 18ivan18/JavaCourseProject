package bg.sofia.uni.fmi.mjt.mss.checkers;

import java.io.File;

import static bg.sofia.uni.fmi.mjt.mss.Constants.WAV_REGEX;

public class FileTypeChecker {
    public static boolean isWavFile(File file) {
        String fileName = file.getName();
        return fileName.matches(WAV_REGEX);
    }
}
