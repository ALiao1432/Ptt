package study.ian.ptt.model.category.category;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String board;
    private List<ArticleInfo> articleInfoList = new ArrayList<>();

    public Category(Document doc) {
        doc.getElementsByClass("board-label").remove(); // remove "看板"
        board = doc.getElementsByClass("board").text();

        Elements articleElements = doc.getElementsByClass("r-ent");
        articleElements.forEach(e ->
                articleInfoList.add(new ArticleInfo(
                        e.getElementsByClass("title").get(0).text(),
                        e.getElementsByClass("title").get(0).getElementsByTag("a").attr("href"),
                        e.getElementsByClass("author").get(0).text(),
                        e.getElementsByClass("item").get(0).getElementsByTag("a").attr("href"),
                        e.getElementsByClass("item").get(1).getElementsByTag("a").attr("href"),
                        e.getElementsByClass("date").get(0).text(),
                        e.getElementsByClass("nrec").get(0).text()
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
        return "Category{" +
                "board='" + board + '\'' +
                ", articleInfoList=" + articleInfoList +
                '}';
    }
}
