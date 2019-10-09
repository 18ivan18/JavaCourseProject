package bg.sofia.uni.fmi.mjt.project.msc.validators;


import static bg.sofia.uni.fmi.mjt.project.msc.Constants.*;

//TODO singleton
public class InputValidator {
    public static boolean validate(String line) {
        return (validateDisconnect(line) || validateAddSongTo(line) || validateCreatePlayList(line) ||
                validateLogin(line) || validateRegister(line) || validateShowPlaylists(line) ||
                validateTop(line) || validateStop(line) || validateSearch(line));
    }

    public static boolean validateDisconnect(String line) {
        return line.equals(DISCONNECT);
    }

    public static boolean validateLogin(String line) {
        return (getFirstWord(line).equals(LOGIN) && line.split(REGEX).length == 3);
    }

    public static boolean validateRegister(String line) {
        return (getFirstWord(line).equals(REGISTER) && line.split(REGEX).length == 3);
    }

    public static boolean validateSearch(String line) {
        //song name multiple words
        return (getFirstWord(line).equals(SEARCH) && line.split(REGEX).length == 2);
    }

    public static boolean validateTop(String line) {
        return (getFirstWord(line).equals(TOP) && line.split(REGEX).length == 2);
    }

    public static boolean validateCreatePlayList(String line) {
        return (getFirstWord(line).equals(CREATE_PLAY_LIST) && line.split(REGEX).length == 2);
    }

    public static boolean validateAddSongTo(String line) {
        return (getFirstWord(line).equals(ADD_SONG_TO) && line.split(REGEX).length == 2);
    }

    public static boolean validateShowPlaylists(String line) {
        return (line.equals(SHOW_PLAYLISTS));
    }

    public static boolean validatePlay(String line) {
        return (getFirstWord(line).equals(PLAY));
    }

    public static boolean validateStop(String line) {
        return (line.equals(STOP));
    }

    private static String getFirstWord(String line) {
        return line.contains(" ") ? line.split(REGEX)[0] : line;
    }

    public static boolean validatePause(String line) {
        return line.equals("pause");
    }

    public static boolean validateContinue(String line) {
        return line.equals("continue");
    }
}

