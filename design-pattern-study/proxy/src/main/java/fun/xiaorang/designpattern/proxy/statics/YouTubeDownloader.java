package fun.xiaorang.designpattern.proxy.statics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">媒体下载应用
 * <p>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a> @Copyright
 * 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a> - show me the code
 * @date 2023/5/10 15:32
 */
public class YouTubeDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeDownloader.class);

    private final ThirdPartyYouTubeLib thirdPartyYouTubeLib;

    public YouTubeDownloader(ThirdPartyYouTubeLib thirdPartyYouTubeLib) {
        this.thirdPartyYouTubeLib = thirdPartyYouTubeLib;
    }

    public void renderVideoPage(String videoId) {
        Video video = thirdPartyYouTubeLib.getVideo(videoId);
        LOGGER.info("渲染视频详情页面");
        LOGGER.info("ID: {}", video.getId());
        LOGGER.info("Title: {}", video.getTitle());
        LOGGER.info("Video: {}", video.getData());
        LOGGER.info("-------------------------------\n");
    }

    public void renderPopularVideos() {
        Map<String, Video> list = thirdPartyYouTubeLib.popularVideos();
        LOGGER.info("渲染热门视频专栏页面");
        for (Video video : list.values()) {
            LOGGER.info("ID: {}  / Title: {}", video.getId(), video.getTitle());
        }
        LOGGER.info("-------------------------------\n");
    }
}
