package com.bigsea.sns.model.test;

import javax.persistence.*;

@Table(name = "gen_test_demo")
public class Demo {
    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 获取用户ID
     *
     * @return id - 用户ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置用户ID
     *
     * @param id 用户ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户姓名
     *
     * @return name - 用户姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户姓名
     *
     * @param name 用户姓名
     */
    public void setName(String name) {
        this.name = name;
    }
}