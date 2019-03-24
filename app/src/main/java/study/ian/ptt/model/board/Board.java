package study.ian.ptt.model.board;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final String TAG = "Board";

    private List<BoardInfo> infoList = new ArrayList<>();

    public Board(Document doc) {
        Elements bElements = doc.getElementsByClass("b-ent");
        bElements.forEach(e -> {
            String count = e.select("div[class=board-nuser]").text();

            infoList.add(new BoardInfo(
                    e.select("a").attr("href"),
                    e.select("div[class=board-name]").text(),
                    e.select("div[class=board-class]").text(),
                    e.select("div[class=board-title]").text(),
                    count.equals("") ? 0 : Integer.valueOf(count)
            ));
        });
    }

    public List<BoardInfo> getInfoList() {
        return infoList;
    }

    @Override
    public String toString() {
        return "Board{" +
                "infoList=" + infoList +
                '}';
    }
}
