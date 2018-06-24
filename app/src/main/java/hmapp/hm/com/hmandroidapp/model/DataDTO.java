package hmapp.hm.com.hmandroidapp.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 〈台区，计量箱，电表综合信息〉
 *
 * @Package com.hm.appservice.DTO
 * @ClassName DataDTO
 * @Author wangky
 * @Create 2018/5/13 15:27
 */
public class DataDTO implements Serializable {
    //台区信息
    private long tgno;
    private String tgname;
    //计量箱信息
    private String meterBoxTgno;
    private String assetNo;
    private String installAddress;
    private String detailAddress;
    private double posX;
    private double posY;
    private long rowNum;
    private long colNum;
    private String collector;
    private Timestamp collDate;
    private long meterBoxStatusCode;
    //电能表信息
    private long boxMeterRela;
    private String meterAssetNo;
    private long rowNo;
    private long colNo;
    private long meterStatusCode;

    public long getTgno() {
        return tgno;
    }

    public void setTgno(long tgno) {
        this.tgno = tgno;
    }

    public String getTgname() {
        return tgname;
    }

    public void setTgname(String tgname) {
        this.tgname = tgname;
    }

    public String getMeterBoxTgno() {
        return meterBoxTgno;
    }

    public void setMeterBoxTgno(String meterBoxTgno) {
        this.meterBoxTgno = meterBoxTgno;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getInstallAddress() {
        return installAddress;
    }

    public void setInstallAddress(String installAddress) {
        this.installAddress = installAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public long getRowNum() {
        return rowNum;
    }

    public void setRowNum(long rowNum) {
        this.rowNum = rowNum;
    }

    public long getColNum() {
        return colNum;
    }

    public void setColNum(long colNum) {
        this.colNum = colNum;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public Timestamp getCollDate() {
        return collDate;
    }

    public void setCollDate(Timestamp collDate) {
        this.collDate = collDate;
    }

    public long getMeterBoxStatusCode() {
        return meterBoxStatusCode;
    }

    public void setMeterBoxStatusCode(long meterBoxStatusCode) {
        this.meterBoxStatusCode = meterBoxStatusCode;
    }

    public long getBoxMeterRela() {
        return boxMeterRela;
    }

    public void setBoxMeterRela(long boxMeterRela) {
        this.boxMeterRela = boxMeterRela;
    }

    public String getMeterAssetNo() {
        return meterAssetNo;
    }

    public void setMeterAssetNo(String meterAssetNo) {
        this.meterAssetNo = meterAssetNo;
    }

    public long getRowNo() {
        return rowNo;
    }

    public void setRowNo(long rowNo) {
        this.rowNo = rowNo;
    }

    public long getColNo() {
        return colNo;
    }

    public void setColNo(long colNo) {
        this.colNo = colNo;
    }

    public long getMeterStatusCode() {
        return meterStatusCode;
    }

    public void setMeterStatusCode(long meterStatusCode) {
        this.meterStatusCode = meterStatusCode;
    }

    @Override
    public String toString() {
        return "DataDTO{" +
                "tgno=" + tgno +
                ", tgname='" + tgname + '\'' +
                ", meterBoxTgno='" + meterBoxTgno + '\'' +
                ", assetNo='" + assetNo + '\'' +
                ", installAddress='" + installAddress + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", posX=" + posX +
                ", posY=" + posY +
                ", rowNum=" + rowNum +
                ", colNum=" + colNum +
                ", collector='" + collector + '\'' +
                ", collDate=" + collDate +
                ", meterBoxStatusCode=" + meterBoxStatusCode +
                ", boxMeterRela=" + boxMeterRela +
                ", meterAssetNo='" + meterAssetNo + '\'' +
                ", rowNo=" + rowNo +
                ", colNo=" + colNo +
                ", meterStatusCode=" + meterStatusCode +
                '}';
    }
}