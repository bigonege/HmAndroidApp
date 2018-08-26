package hmapp.hm.com.hmandroidapp.model;

import java.util.Date;

public class Tginfo {
    private Long tgno;

    private String tgname;

    private Date createTime;

    public Long getTgno() {
        return tgno;
    }

    public void setTgno(Long tgno) {
        this.tgno = tgno;
    }

    public String getTgname() {
        return tgname;
    }

    public void setTgname(String tgname) {
        this.tgname = tgname == null ? null : tgname.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}