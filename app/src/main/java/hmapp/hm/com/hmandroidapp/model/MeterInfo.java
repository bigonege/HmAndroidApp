package hmapp.hm.com.hmandroidapp.model;

import java.util.Date;

public class MeterInfo {
    private Long id;

    private Long boxMeterRela;

    private String assetNo;

    private String installAddress;

    private String detailAddress;

    private Float posX;

    private Float posY;

    private Integer rowNo;

    private Integer colNo;

    private Integer statusCode;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBoxMeterRela() {
        return boxMeterRela;
    }

    public void setBoxMeterRela(Long boxMeterRela) {
        this.boxMeterRela = boxMeterRela;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo == null ? null : assetNo.trim();
    }

    public String getInstallAddress() {
        return installAddress;
    }

    public void setInstallAddress(String installAddress) {
        this.installAddress = installAddress == null ? null : installAddress.trim();
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress == null ? null : detailAddress.trim();
    }

    public Float getPosX() {
        return posX;
    }

    public void setPosX(Float posX) {
        this.posX = posX;
    }

    public Float getPosY() {
        return posY;
    }

    public void setPosY(Float posY) {
        this.posY = posY;
    }

    public Integer getRowNo() {
        return rowNo;
    }

    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

    public Integer getColNo() {
        return colNo;
    }

    public void setColNo(Integer colNo) {
        this.colNo = colNo;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MeterInfo{" +
                "id=" + id +
                ", boxMeterRela=" + boxMeterRela +
                ", assetNo='" + assetNo + '\'' +
                ", installAddress='" + installAddress + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", posX=" + posX +
                ", posY=" + posY +
                ", rowNo=" + rowNo +
                ", colNo=" + colNo +
                ", statusCode=" + statusCode +
                ", createTime=" + createTime +
                '}';
    }
}