package fun.xiaorang.mybatis.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/20 16:07
 */
public class MyDruidDataSourceFactory extends PooledDataSourceFactory {
    private DataSource dataSource;

    @Override
    public void setProperties(Properties properties) {
        try {
            this.dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException("init datasource error", e);
        }
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }
}