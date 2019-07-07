package com.myself.CC.crawler.common;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

//转换数据抽象成DataSet类，存储清洗的数据
@ToString
public class DataSet {
    /*
     * data 把DOM解析清洗之后存储的数据,我们真正需要的数据
     * 比如：
     *   标题：XXX
     *   作者：XXX
     *   正文：XXX
     * */
    private Map<String, Object> data = new HashMap<>();

    public void putData(String key, Object value) {
        this.data.put(key, value);
    }

    //获取单个信息
    public Object getData(String key) {
        return this.data.get(key);
    }

    //获取所有信息   防止他人随意破坏信息，new一个HashMap
    public Map<String, Object> getAllData() {
        return new HashMap<>(this.data);
    }
}
