package study.ian.ptt.model.article;

public class Push {

    private String pushTag;
    private String author;
    private String content;
    private String time;
    private int pushTagCount;

    public Push(String pushTag, String author, String content, String time, int pushTagCount) {
        this.pushTag = pushTag;
        this.author = author;
        this.content = content;
        this.time = time;
        this.pushTagCount = pushTagCount;
    }

    public String getPushTag() {
        return pushTag;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public int getPushTagCount() {
        return pushTagCount;
    }

    @Override
    public String toString() {
        return "Push{" +
                "pushTag='" + pushTag + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", pushTagCount=" + pushTagCount +
                '}';
    }
}
