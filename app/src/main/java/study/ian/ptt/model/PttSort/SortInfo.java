package study.ian.ptt.model.PttSort;

public class SortInfo {

    private static final int SORT_UNDEFINE = -1;
    private static final int SORT_BOARD = 0;
    private static final int SORT_CLASS = 1;

    private String href;
    private String name;
    private String boardClass;
    private String boardTitle;
    private String userCount;
    private int sort;

    SortInfo(String href, String name, String boardClass, String boardTitle, String userCount) {
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

    public String getUserCount() {
        return userCount;
    }

    @Override
    public String toString() {
        return "SortInfo{" +
                "href='" + href + '\'' +
                ", name='" + name + '\'' +
                ", boardClass='" + boardClass + '\'' +
                ", boardTitle='" + boardTitle + '\'' +
                ", userCount='" + userCount + '\'' +
                ", sort=" + sort +
                '}';
    }
}
