package study.ian.ptt.model.board;

public class BoardInfo {

    public static final int SORT_UNDEFINE = -1;
    public static final int SORT_BOARD = 0;
    public static final int SORT_CLASS = 1;

    private String href;
    private String name;
    private String boardClass;
    private String boardTitle;
    private int userCount;
    private int sort;

    public BoardInfo(String href, String name, String boardClass, String boardTitle, int userCount) {
        this.href = href;
        this.name = name;
        this.boardClass = boardClass;
        this.boardTitle = boardTitle;
        this.userCount = userCount;

        if (href.startsWith("/cls")) {
            sort = SORT_CLASS;
        } else if (href.startsWith("/bbs")) {
            sort = SORT_BOARD;
        } else {
            sort = SORT_UNDEFINE;
        }
    }

    public int getSort() {
        return sort;
    }

    public String getHref() {
        return href;
    }

    public String getName() {
        return name;
    }

    public String getBoardClass() {
        return boardClass;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public int getUserCount() {
        return userCount;
    }

    @Override
    public String toString() {
        return "BoardInfo{" +
                "href='" + href + '\'' +
                ", name='" + name + '\'' +
                ", boardClass='" + boardClass + '\'' +
                ", boardTitle='" + boardTitle + '\'' +
                ", userCount='" + userCount + '\'' +
                ", sort=" + sort +
                '}';
    }
}
