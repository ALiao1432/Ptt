package study.ian.ptt.model.category;

import android.util.Log;

import study.ian.ptt.service.ServiceBuilder;

public class ArticleInfo {

    private final String TAG = "ArticleInfo";

    private String title;
    private String href;
    private String author;
    private String sameTitleHref;
    private String sameAuthorHref;
    private String date;
    private String mark;
    private String count;
    private boolean isRead = false;

    public ArticleInfo(
            String title,
            String href,
            String author,
            String sameTitleHref,
            String sameAuthorHref,
            String date,
            String mark,
            String count) {
        this.title = title;
        this.href = href;
        this.author = author;
        this.sameTitleHref = sameTitleHref;
        this.sameAuthorHref = sameAuthorHref;
        this.date = date;
        this.mark = mark;
        this.count = count;

        if (!this.sameTitleHref.equals("")) {
            this.sameTitleHref = this.sameTitleHref.substring(5);
        }
        if (!this.sameAuthorHref.equals("")) {
            this.sameAuthorHref = this.sameAuthorHref.substring(5);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

    public String getAuthor() {
        return author;
    }

    public String getSameTitleHref() {
        return sameTitleHref;
    }

    public String getSameAuthorHref() {
        return sameAuthorHref;
    }

    public String getDate() {
        return date;
    }

    public String getCount() {
        return count;
    }

    public String getMark() {
        return mark;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "ArticleInfo{" +
                "title='" + title + '\'' +
                ", href='" + href + '\'' +
                ", author='" + author + '\'' +
                ", sameTitleHref='" + sameTitleHref + '\'' +
                ", sameAuthorHref='" + sameAuthorHref + '\'' +
                ", date='" + date + '\'' +
                ", count='" + count + '\'' +
                ", mark='" + mark + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
