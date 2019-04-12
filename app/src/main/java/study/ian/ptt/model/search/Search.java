package study.ian.ptt.model.search;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import study.ian.ptt.model.category.ArticleInfo;

class Search {

    private final String TAG = "Search";
    private final String board;
    private final List<ArticleInfo> articleInfoList = new ArrayList<>();

    public Search(Document doc) {
        doc.getElementsByClass("board-label").remove(); // remove "看板"
        board = doc.getElementsByClass("board").text();

        Elements articleElements = doc.getElementsByClass("r-ent");
        for (int i = articleElements.size() - 1; i >= 0; i--) {
            Element e = articleElements.get(i);

            articleInfoList.add(new ArticleInfo(
                    e.select("div[class=title]").text(),
                    e.select("div[class=title] > a").attr("href"),
                    e.select("div[class=author]").text().trim(),
                    e.select("div[class=dropdown] > div[class=item]:nth-child(1) > a").attr("href"),
                    e.select("div[class=dropdown] > div[class=item]:nth-child(2) > a").attr("href"),
                    " +" + e.select("div[class=date]").text().trim(),
                    e.select("div[class=mark]").text(),
                    e.select("div[class=board-nuser]").text()
            ));
        }
    }

    public String getBoard() {
        return board;
    }

    public List<ArticleInfo> getArticleInfoList() {
        return articleInfoList;
    }

    @NotNull
    @Override
    public String toString() {
        return "Search{" +
                "board='" + board + '\'' +
                ", articleInfoList=" + articleInfoList +
                '}';
    }
}







