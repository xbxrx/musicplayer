package Model;

public class Music {
    private int number;
    private String title;
    private String author;
    int cover;
    int musicId;

    public Music(int number,String title,String author,int cover,int musicId){
        this.number=number;
        this.title=title;
        this.author=author;
        this.cover=cover;
        this.musicId=musicId;
    }


    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public int getNumber() {
        return number;
    }

    public int getMusicId() {
        return musicId;
    }

    public int getCover() {
        return cover;
    }
}
