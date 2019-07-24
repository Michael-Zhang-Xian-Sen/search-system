package nju.software.ocr.model;

import java.util.List;

public class OcrRoot {
    private String ocrText;
    private String pdfURL;
    private List<Ocr> textResult;
    private String fileName;

    public void setOcrText(String ocrText) {
        this.ocrText = ocrText;
    }
    public String getOcrText() {
        return ocrText;
    }

    public void setPdfURL(String pdfURL) {
        this.pdfURL = pdfURL;
    }
    public String getPdfURL() {
        return pdfURL;
    }

    public void setTextResult(List<Ocr> textResult) {
        this.textResult = textResult;
    }
    public List<Ocr> getTextResult() {
        return textResult;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "OcrRoot{" +
                "ocrText='" + ocrText + '\'' +
                ", pdfURL='" + pdfURL + '\'' +
                ", textResult=" + textResult +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}