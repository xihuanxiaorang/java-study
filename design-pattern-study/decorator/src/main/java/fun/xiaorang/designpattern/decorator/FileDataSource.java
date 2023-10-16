package fun.xiaorang.designpattern.decorator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/16 2:23
 */
public class FileDataSource implements DataSource {
    private final String fileName;

    public FileDataSource(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void writeData(String data) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data.getBytes(), 0, data.length());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readData() {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            return new String(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
