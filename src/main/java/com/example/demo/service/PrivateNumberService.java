package com.example.demo.service;

import java.util.List;

public interface PrivateNumberService<HiddenNumberBean> {


    /**
     * 根据 A 和 B 号，分配隐私号
     * @param a A 号
     * @param b B 号
     * @return 隐私号
     * @throws Exception 分配号码失败，注意有多种可能
     */
    String numberMalloc(String a, String b ,String userfulXNumber) throws Exception;
    /**
     * 释放隐私号
     * @param uuid 隐私号唯一识别码
     * @throws Exception 释放号码失败
     */
//    void free1(String uuid) throws Exception;

    /**
     * 根据唯一识别码，查询隐私号
     * @param uuid 隐私号唯一识别码
     * @return 隐私号，没找到为 null
     * @throws Exception 查询号码失败
     */
    HiddenNumberBean querySelect(String uuid) throws Exception;
//
    /**
     * 根据 A 和 B 号，查找隐私号
     * @param a A 号
     * @param b B 号
     * @return 隐私号 X，没找到为 null
     * @throws Exception 查询号码失败
     */
   // HiddenNumberBean querynNumberSelect(String a, String b) throws Exception;
//
    /**
     * 查询并返回所有当前在 X 号上绑定的隐私号
     * @param x 隐私号
     * @return 所有在 x 上已绑定的隐私号
     * @throws Exception 找失败了
     */
    List<HiddenNumberBean> queryByX(String x) throws Exception;
//
//    class PrivateNumberLog {
//
//    }
//
//
//    List<PrivateNumberLog> queryLog() throws Exception;
//
//    class PrivateNumberEventLog {
//
//    }
//
//    List<PrivateNumberEventLog> queryEventLog(String uuid) throws Exception;
}
