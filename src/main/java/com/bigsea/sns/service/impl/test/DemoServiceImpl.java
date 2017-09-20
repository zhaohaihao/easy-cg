package com.bigsea.sns.service.impl.test;

import com.bigsea.sns.dao.mapper.test.DemoMapper;
import com.bigsea.sns.model.test.Demo;
import com.bigsea.sns.service.test.DemoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 *
 * Created by zhh on 2017/09/20.
 */
@Service
public class DemoServiceImpl implements DemoService {
    @Resource
    private DemoMapper genTestDemoMapper;

}
