package com.wy.basic.service;

import com.wy.basic.model.AppConfig;
import com.wy.basic.tools.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by zsl-pc on 2016/9/7.
 */
@Component
public class AppConfigServiceImpl {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private IAppConfigService appConfigService;

    public void addOrUpdate(AppConfig ac) {
        AppConfig a = appConfigService.loadOne();
        if(a==null) {
            appConfigService.save(ac);
        } else {
            MyBeanUtils.copyProperties(ac, a, new String[]{"id"});
            appConfigService.save(a);
        }
    }
}
