package study.ian.ptt.model.board;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final String TAG = "Board";

    private final List<BoardInfo> infoList = new ArrayList<>();

    public Board(Document doc) {
        Elements bElements = doc.getElementsByClass("b-ent");
        bElements.forEach(e ->
                infoList.add(new BoardInfo(
                        e.select("a").attr("href"),
                        e.select("div[class=board-name]").text(),
                        e.select("div[class=board-class]").text(),
                        e.select("div[class=board-title]").text(),
                        e.select("div[class=board-nuser]").text()
                ))
        );
    }

    public List<BoardInfo> getInfoList() {
        return infoList;
    }

    @NotNull
    @Override
    public String toString() {
        return "Board{" +
                "infoList=" + infoList +
                '}';
    }
}
