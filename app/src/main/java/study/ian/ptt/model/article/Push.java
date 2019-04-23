package study.ian.ptt.model.article;

import org.jetbrains.annotations.NotNull;

public class Push {

    private final String pushTag;
    private final String author;
    private final String content;
    private final String time;
    private final int pushTagCount;

    Push(String pushTag, String author, String content, String time, int pushTagCount) {
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

    @NotNull
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
