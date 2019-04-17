package study.ian.ptt.model.poll;

import com.google.gson.annotations.SerializedName;

public class Poll {

    @SerializedName("contentHtml")
    private String contentHtml;

    @SerializedName("pollUrl")
    private String pollUrl;

    @SerializedName("success")
    private boolean success;

    public String getContentHtml() {
        return contentHtml;
    }

    public String getPollUrl() {
        return pollUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "Poll{" +
                "contentHtml='" + contentHtml + '\'' +
                ", pollUrl='" + pollUrl + '\'' +
                ", success=" + success +
                '}';
    }
}
