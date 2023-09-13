package fun.xiaorang.designpattern.proxy.statics;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">视频文件
 * <p>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a> @Copyright
 * 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a> - show me the code
 * @date 2023/5/10 15:06
 */
public class Video {
    private String id;
    private String title;
    private String data = "Random video.";

    public Video() {
    }

    public Video(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
