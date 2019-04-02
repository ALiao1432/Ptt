package study.ian.ptt.model.category;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
        for (int i = articleElements.size() - 1; i >= 0; i--) {
            Element e = articleElements.get(i);

            articleInfoList.add(new ArticleInfo(
                    e.select("div[class=title]").text(),
                    e.select("div[class=title] > a").attr("href"),
                    e.select("div[class=author]").text(),
                    e.select("div[class=dropdown] > div[class=item]:nth-child(1) > a").attr("href"),
                    e.select("div[class=dropdown] > div[class=item]:nth-child(2) > a").attr("href"),
                    " +" + e.select("div[class=date]").text().trim(),
                    e.select("div[class=mark]").text(),
                    e.select("div[class=nrec]").text()
            ));
        }
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
