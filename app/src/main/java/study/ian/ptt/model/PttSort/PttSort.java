package study.ian.ptt.model.PttSort;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class PttSort {

    private final String TAG = "PttSort";

    private List<SortInfo> infoList = new ArrayList<>();

    public PttSort(Document doc) {
        Elements bElements = doc.getElementsByClass("b-ent");
        bElements.forEach(e ->
                infoList.add(new SortInfo(
                        e.select("a").attr("href"),
                        e.select("div[class=board-name]").text(),
                        e.select("div[class=board-class]").text(),
                        e.select("div[class=board-title]").text(),
                        e.select("div[class=board-nuser]").text()
                ))
        );
    }

    public List<SortInfo> getInfoList() {
        return infoList;
    }

    @Override
    public String toString() {
        return "PttSort{" +
                "infoList=" + infoList +
                '}';
    }
}
