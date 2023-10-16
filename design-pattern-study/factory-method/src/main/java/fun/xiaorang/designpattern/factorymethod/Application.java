package fun.xiaorang.designpattern.factorymethod;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/10 7:51
 */
public class Application {
    private static Dialog dialog;

    public static void main(String[] args) {
        initialize();
        runBusinessLogic();
    }

    private static void runBusinessLogic() {
        dialog.render();
    }

    /**
     * 根据当前配置或环境设定选择创建者的类型
     */
    private static void initialize() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            dialog = new WindowsDialog();
        } else if (osName.contains("web")) {
            dialog = new HtmlDialog();
        } else {
            throw new RuntimeException("未知操作系统");
        }
    }
}
