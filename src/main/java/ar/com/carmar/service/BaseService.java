package ar.com.carmar.service;


import ar.com.carmar.entity.IAuditable;

import java.util.Date;

public class BaseService {

    public BaseService() {}

    public void auditar(IAuditable domain, String userDomain) {
        if (domain.getAudFechaIns() == null && domain.getAudUsrIns() == null) {
            domain.setAudFechaIns(new Date());
            domain.setAudUsrIns(userDomain);
        }

        domain.setAudFechaUpd(new Date());
        domain.setAudUsrUpd(userDomain);
    }
}
