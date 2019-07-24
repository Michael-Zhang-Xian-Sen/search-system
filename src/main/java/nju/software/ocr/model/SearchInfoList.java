package nju.software.ocr.model;

public class SearchInfoList {
    private String ocrInfo;
    private int hits;

    public String getOcrInfo() {
        return ocrInfo;
    }

    public void setOcrInfo(String ocrInfo) {
        this.ocrInfo = ocrInfo;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "SearchInfoList{" +
                "ocrInfo='" + ocrInfo + '\'' +
                ", hits=" + hits +
                '}';
    }
}
