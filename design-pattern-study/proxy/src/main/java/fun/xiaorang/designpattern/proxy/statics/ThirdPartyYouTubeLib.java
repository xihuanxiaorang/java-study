package fun.xiaorang.designpattern.proxy.statics;

import java.util.Map;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">YouTube远程服务接口
 * <p>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a> @Copyright
 * 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a> - show me the code
 * @date 2023/5/10 15:05
 */
public interface ThirdPartyYouTubeLib {
    /**
     * 获取热门视频
     *
     * @return 热门视频集合
     */
    Map<String, Video> popularVideos();

    /**
     * 根据视频id获取视频详细信息
     *
     * @param videoId 视频id
     * @return 视频详细信息
     */
    Video getVideo(String videoId);
}
