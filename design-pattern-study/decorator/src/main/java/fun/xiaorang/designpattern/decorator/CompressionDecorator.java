package fun.xiaorang.designpattern.decorator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/16 2:50
 */
public class CompressionDecorator extends DataSourceDecorator {
    private int compressionLevel = 6;

    public CompressionDecorator(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void writeData(String data) {
        super.writeData(compress(data));
    }

    @Override
    public String readData() {
        return decompress(super.readData());
    }

    private String compress(String stringData) {
        byte[] data = stringData.getBytes();
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream(512)) {
            // 此处需要先关闭dos，因为dos是缓冲的，需要先关闭才会将数据写入到bout中
            // 否则的话会因为数据不完整导致解压失败，抛出 EOFException: Unexpected end of ZLIB input stream 异常
            try (DeflaterOutputStream dos = new DeflaterOutputStream(bout, new Deflater(compressionLevel))) {
                dos.write(data);
            }
            return Base64.getEncoder().encodeToString(bout.toByteArray());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String decompress(String stringData) {
        byte[] data = Base64.getDecoder().decode(stringData);
        try (InputStream in = new ByteArrayInputStream(data);
             InflaterInputStream iin = new InflaterInputStream(in);
             ByteArrayOutputStream bout = new ByteArrayOutputStream(512)) {
            int b;
            while ((b = iin.read()) != -1) {
                bout.write(b);
            }
            return bout.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int getCompressionLevel() {
        return compressionLevel;
    }

    public void setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }
}
