package com.bigsea.sns.service.impl.test;

import com.bigsea.sns.dao.mapper.test.DemoMapper;
import com.bigsea.sns.model.test.Demo;
import com.bigsea.sns.service.test.DemoService;
import com.bigsea.sns.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * Created by zhh on 2017/09/27.
 */
@Service
public class DemoServiceImpl extends AbstractService<Demo> implements DemoService {

    @Autowired
    private DemoMapper demoMapper;

}
