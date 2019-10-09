package bg.sofia.uni.fmi.mjt.mss.music;

public class Song {
    //TODO Hash
    private String name;
    private String author;
    private String pathToSong;

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Song(String name, String author, String pathToSong) {
        this.name = name;
        this.author = author;
        this.pathToSong = pathToSong;
    }

    public String getPathToSong() {
        return pathToSong;
    }

    @Override
    public String toString() {
        return author + " - " + name;
    }
}
