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

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/20 17:00
 */
class ArticleMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(ArticleMapperTest.class);

    @Test
    void findOne() {
        try (InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml")) {
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                ArticleMapper articleMapper = sqlSession.getMapper(ArticleMapper.class);
                Article article = articleMapper.findOne(1);
                Author author = article.getAuthor();
                article.setAuthor(null);
                logger.info("article：{}", article);
                logger.info("author：{}", author);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}