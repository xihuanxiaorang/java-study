package fun.xiaorang.designpattern.factorymethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/10 7:30
 */
public class HtmlButton implements Button {
    private static final Logger logger = LoggerFactory.getLogger(HtmlButton.class);

    @Override
    public void render() {
        logger.info("<button>Test Button</button>");
        this.onClick();
    }

    @Override
    public void onClick() {
        logger.info("Click! Button says - 'Hello World!'");
    }
}
