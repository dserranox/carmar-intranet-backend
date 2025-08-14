package ar.com.carmar.entity;

import java.util.Date;

public interface IAuditable {

    String getAudUsrIns();
    void setAudUsrIns(String var1);
    Date getAudFechaIns();
    void setAudFechaIns(Date var1);
    String getAudUsrUpd();
    void setAudUsrUpd(String var1);
    Date getAudFechaUpd();
    void setAudFechaUpd(Date var1);
}
