package fun.xiaorang.designpattern.decorator;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/16 2:28
 */
class ApiTest {
    private static final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test() {
        String salaryRecords = "Name,Salary\nJohn Smith,100000\nSteven Jobs,912000";
        DataSource plain = new FileDataSource("target/test.txt");
        DataSourceDecorator encoded = new CompressionDecorator(
                new EncryptionDecorator(plain));
        encoded.writeData(salaryRecords);
        logger.info("- Input ----------------");
        logger.info(salaryRecords);
        logger.info("- Encoded --------------");
        logger.info(plain.readData());
        logger.info("- Decoded --------------");
        logger.info(encoded.readData());
    }
}