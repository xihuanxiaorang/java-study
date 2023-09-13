package fun.xiaorang.spring.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/22 22:04
 */
@Data
public class Customer implements Serializable {
    private String name;
    private Integer age;
    private String[] emails;
    private Set<String> tels;
    private List<String> addresses;
    private Map<String, String> qqs;
    private Properties p;
    private Date recordDate;
}
