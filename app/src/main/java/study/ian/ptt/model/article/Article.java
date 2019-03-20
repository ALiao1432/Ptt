package study.ian.ptt.model.article;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import study.ian.ptt.util.ContentConverter;

public class Article {

    private final String TAG = "Article";

    private String author;
    private String board;
    private String title;
    private String articleTime;
    private String mainContent;
    private List<Push> pushList = new ArrayList<>();
    private int[] pushTagCount = {0, 0, 0}; // {推, →, 噓}

    public Article(Document doc) {
        author = doc.select("#main-content > div:nth-child(1) > span.article-meta-value").text();
        board = doc.select("#main-content > div.article-metaline-right > span.article-meta-value").text();
        title = doc.select("#main-content > div:nth-child(3) > span.article-meta-value").text();
        articleTime = doc.select("#main-content > div:nth-child(4) > span.article-meta-value").text();

        Elements pushElements = doc.getElementsByClass("push");
        pushElements.forEach(e -> {
            switch (e.child(0).text()) {
                case "推":
                    pushTagCount[0]++;
                    pushList.add(new Push(
                            e.child(0).text(),
                            e.child(1).text().trim(),
                            e.child(2).text().substring(2),
                            e.child(3).text().trim(),
                            pushTagCount[0]
                    ));
                    break;
                case "→":
                    pushTagCount[1]++;
                    pushList.add(new Push(
                            e.child(0).text(),
                            e.child(1).text().trim(),
                            e.child(2).text().substring(2),
                            e.child(3).text().trim(),
                            pushTagCount[1]
                    ));
                    break;
                case "噓":
                    pushTagCount[2]++;
                    pushList.add(new Push(
                            e.child(0).text(),
                            e.child(1).text().trim(),
                            e.child(2).text().substring(2),
                            e.child(3).text().trim(),
                            pushTagCount[2]
                    ));
                    break;
            }
        });

        doc.getElementsByClass("article-metaline").remove();
        doc.getElementsByClass("article-metaline-right").remove();
        doc.getElementsByClass("push").remove();
        doc.outputSettings(new Document.OutputSettings().prettyPrint(false));

        Elements rElements = doc.getElementsByClass("richcontent");
        Elements prevElements = rElements.prev();

        rElements.tagName("img");
        rElements.removeAttr("class");
        rElements.empty();
        for (int i = 0; i < rElements.size(); i++) {
            String src = prevElements.get(i).attr("href");
            rElements.get(i).attr("src", src);
        }
        rElements.append("</br></br>");

        Element mainElement = doc.getElementById("main-content");
        mainContent = mainElement.toString();
        mainContent = ContentConverter.classToStyle(mainContent);
        mainContent = ContentConverter.newLineToBr(mainContent);
    }

    public String getTAG() {
        return TAG;
    }

    public String getAuthor() {
        return author;
    }

    public String getBoard() {
        return board;
    }

    public String getTitle() {
        return title;
    }

    public String getArticleTime() {
        return articleTime;
    }

    public String getMainContent() {
        return mainContent;
    }

    public List<Push> getPushList() {
        return pushList;
    }

    @Override
    public String toString() {
        return "Article{" +
                "TAG='" + TAG + '\'' +
                ", author='" + author + '\'' +
                ", board='" + board + '\'' +
                ", title='" + title + '\'' +
                ", articleTime='" + articleTime + '\'' +
                ", mainContent='" + mainContent + '\'' +
                ", pushList=" + pushList +
                '}';
    }
}
