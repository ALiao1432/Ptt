package study.ian.ptt.model.search;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import study.ian.ptt.model.category.ArticleInfo;

public class Search {

    private final String TAG = "Search";
    private String board;
    private List<ArticleInfo> articleInfoList = new ArrayList<>();

    public Search(Document doc) {
        doc.getElementsByClass("board-label").remove(); // remove "看板"
        board = doc.getElementsByClass("board").text();

        Elements articleElements = doc.getElementsByClass("r-ent");
        articleElements.forEach(e ->
                articleInfoList.add(new ArticleInfo(
                        e.select("div[class=title]").text(),
                        e.select("div[class=title] > a").attr("href"),
                        e.select("div[class=author]").text(),
                        e.select("div[class=dropdown] > div[class=item]:nth-child(1) > a").attr("href"),
                        e.select("div[class=dropdown] > div[class=item]:nth-child(2) > a").attr("href"),
                        e.select("div[class=date]").text(),
                        e.select("div[class=mark]").text(),
                        e.select("div[class=nrec]").text()
                ))
        );
    }

    public String getBoard() {
        return board;
    }

    public List<ArticleInfo> getArticleInfoList() {
        return articleInfoList;
    }

    @Override
    public String toString() {
        return "Search{" +
                "board='" + board + '\'' +
                ", articleInfoList=" + articleInfoList +
                '}';
    }
}
