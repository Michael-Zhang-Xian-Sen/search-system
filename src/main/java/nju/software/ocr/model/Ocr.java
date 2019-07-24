/**
 * Copyright 2019 bejson.com
 */
package nju.software.ocr.model;

/**
 * Auto-generated: 2019-07-19 23:46:51
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Ocr {

    private String charNum;
    private String isHandwritten;
    private String leftBottom;
    private String leftTop;
    private String rightBottom;
    private String rightTop;
    private String text;

    public void setCharNum(String charNum) {
        this.charNum = charNum;
    }
    public String getCharNum() {
        return charNum;
    }

    public void setIsHandwritten(String isHandwritten) {
        this.isHandwritten = isHandwritten;
    }
    public String getIsHandwritten() {
        return isHandwritten;
    }

    public void setLeftBottom(String leftBottom) {
        this.leftBottom = leftBottom;
    }
    public String getLeftBottom() {
        return leftBottom;
    }

    public void setLeftTop(String leftTop) {
        this.leftTop = leftTop;
    }
    public String getLeftTop() {
        return leftTop;
    }

    public void setRightBottom(String rightBottom) {
        this.rightBottom = rightBottom;
    }
    public String getRightBottom() {
        return rightBottom;
    }

    public void setRightTop(String rightTop) {
        this.rightTop = rightTop;
    }
    public String getRightTop() {
        return rightTop;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Ocr{" +
                "charNum='" + charNum + '\'' +
                ", isHandwritten='" + isHandwritten + '\'' +
                ", leftBottom='" + leftBottom + '\'' +
                ", leftTop='" + leftTop + '\'' +
                ", rightBottom='" + rightBottom + '\'' +
                ", rightTop='" + rightTop + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}