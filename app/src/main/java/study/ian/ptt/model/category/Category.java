package study.ian.ptt.model.category;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private final String TAG = "Category";
    private String board;
    private String lastPage;
    private String newestPage;
    private String nextPage;
    private String prePage;
    private List<ArticleInfo> articleInfoList = new ArrayList<>();

    public Category(Document doc) {
        doc.getElementsByClass("board-label").remove(); // remove "看板"
        board = doc.getElementsByClass("board").text();

        Elements pageElements = doc.getElementsByClass("btn-group btn-group-paging");
        lastPage = pageElements.get(0).getElementsByTag("a").get(0).attr("href");
        prePage = pageElements.get(0).getElementsByTag("a").get(1).attr("href");
        nextPage = pageElements.get(0).getElementsByTag("a").get(2).attr("href");
        newestPage = pageElements.get(0).getElementsByTag("a").get(3).attr("href");

        Elements articleElements = doc.getElementsByClass("r-ent");
        articleElements.forEach(e ->
                articleInfoList.add(new ArticleInfo(
                        e.getElementsByClass("title").get(0).text(),
                        e.getElementsByClass("title").get(0).getElementsByTag("a").attr("href"),
                        e.getElementsByClass("author").get(0).text(),
                        e.getElementsByClass("item").get(0).getElementsByTag("a").attr("href"),
                        e.getElementsByClass("item").get(1).getElementsByTag("a").attr("href"),
                        e.getElementsByClass("date").get(0).text(),
                        e.getElementsByClass("mark").get(0).text(),
                        e.getElementsByClass("nrec").get(0).text()
                ))
        );
    }

    public String getBoard() {
        return board;
    }

    public String getLastPage() {
        return lastPage;
    }

    public String getNewestPage() {
        return newestPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public String getPrePage() {
        return prePage;
    }

    public List<ArticleInfo> getArticleInfoList() {
        return articleInfoList;
    }

    @Override
    public String toString() {
        return "Category{" +
                "board='" + board + '\'' +
                ", lastPage='" + lastPage + '\'' +
                ", newestPage='" + newestPage + '\'' +
                ", nextPage='" + nextPage + '\'' +
                ", prePage='" + prePage + '\'' +
                ", articleInfoList=" + articleInfoList +
                '}';
    }
}
