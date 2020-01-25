package com.github.hairless.plink.service.tools;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author aven danxieai@163.com
 * @version 0.1
 * @date 2020/1/24
 */
public class FastJsonTest {

    @Test
    public void injectUnderlineValue() {
        String value = "{\"firstName\":\"lisi\",\"first_name\":\"zhangsan\",\"lastName\":\"wang\"}";
        Person jsonObject = JSONObject.parseObject(value, Person.class);
        System.out.println(jsonObject);
        Assert.assertEquals("zhangsan", jsonObject.getFirstName());
    }
    @Test
    public void test() {
        System.out.println(System.getProperty("user.dir"));
    }
    @Data
    public static class Person {

        private String firstName;

        private String lastName;

        private String age;



    }
}
