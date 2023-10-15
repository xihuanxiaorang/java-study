package fun.xiaorang.designpattern.decorator;

import java.util.Base64;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/16 2:39
 */
public class EncryptionDecorator extends DataSourceDecorator {
    public EncryptionDecorator(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void writeData(String data) {
        super.writeData(encode(data));
    }

    @Override
    public String readData() {
        return decode(super.readData());
    }

    private String encode(String data) {
        byte[] results = data.getBytes();
        for (int i = 0; i < results.length; i++) {
            results[i] += (byte) 1;
        }
        return Base64.getEncoder().encodeToString(results);
    }

    private String decode(String data) {
        byte[] results = Base64.getDecoder().decode(data);
        for (int i = 0; i < results.length; i++) {
            results[i] -= (byte) 1;
        }
        return new String(results);
    }
}
