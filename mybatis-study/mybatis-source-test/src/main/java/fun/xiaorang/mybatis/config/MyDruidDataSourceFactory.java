package fun.xiaorang.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;

import java.util.Properties;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/20 16:07
 */
public class MyDruidDataSourceFactory extends PooledDataSourceFactory {
    public MyDruidDataSourceFactory() {
        this.dataSource = new DruidDataSource();
    }

    @Override
    public void setProperties(Properties properties) {
        try {
            DruidDataSourceFactory.config((DruidDataSource) dataSource, properties);
        } catch (Exception e) {
            throw new RuntimeException("init datasource error", e);
        }
    }
}