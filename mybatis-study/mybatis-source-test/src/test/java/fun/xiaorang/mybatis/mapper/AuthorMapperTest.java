package fun.xiaorang.mybatis.mapper;

import fun.xiaorang.mybatis.entity.Article;
import fun.xiaorang.mybatis.entity.Author;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/20 16:57
 */
class AuthorMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(AuthorMapperTest.class);

    @Test
    public void findOne() {
        try (InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml")) {
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                AuthorMapper authorMapper = sqlSession.getMapper(AuthorMapper.class);
                Author author = authorMapper.findOne(1);
                List<Article> articles = author.getArticles();
                author.setArticles(null);
                logger.info("author：{}", author);
                logger.info("articles：{}", articles);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}