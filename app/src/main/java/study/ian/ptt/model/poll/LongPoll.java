package study.ian.ptt.model.poll;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class LongPoll {

    @SerializedName("brdname")
    private String brdname;

    @SerializedName("filename")
    private String filename;

    @SerializedName("size")
    private String size;

    @SerializedName("sig")
    private String sig;

    @SerializedName("cacheKey")
    private String cacheKey;

    public String getBrdname() {
        return brdname;
    }

    public String getFilename() {
        return filename;
    }

    public String getSize() {
        return size;
    }

    public String getSig() {
        return sig;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    @NotNull
    @Override
    public String toString() {
        return "LongPoll {" +
                "brdname='" + brdname + '\'' +
                ", filename='" + filename + '\'' +
                ", size=" + size +
                ", sig='" + sig + '\'' +
                ", cacheKey='" + cacheKey + '\'' +
                '}';
    }
}
