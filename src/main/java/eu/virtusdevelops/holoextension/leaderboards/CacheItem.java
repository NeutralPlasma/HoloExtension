package eu.virtusdevelops.holoextension.leaderboards;

public class CacheItem {
    private int position;
    private String board;

    public CacheItem(int position, String board){
        this.board = board;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getBoard() {
        return board;
    }
}
