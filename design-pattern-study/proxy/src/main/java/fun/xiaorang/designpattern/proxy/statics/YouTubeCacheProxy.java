package fun.xiaorang.designpattern.proxy.statics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">缓存代理
 * <p>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a> @Copyright
 * 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a> - show me the code
 * @date 2023/5/10 15:23
 */
public class YouTubeCacheProxy implements ThirdPartyYouTubeLib {
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeCacheProxy.class);
    private final ThirdPartyYouTubeLib thirdPartyYouTubeLib;
    private final Map<String, Video> cacheAll = new HashMap<>();
    private Map<String, Video> cachePopular = new HashMap<>();

    public YouTubeCacheProxy(ThirdPartyYouTubeLib thirdPartyYouTubeLib) {
        this.thirdPartyYouTubeLib = thirdPartyYouTubeLib;
    }

    @Override
    public Map<String, Video> popularVideos() {
        if (cachePopular.isEmpty()) {
            cachePopular = thirdPartyYouTubeLib.popularVideos();
        } else {
            LOGGER.info("从缓存中下载热门视频");
        }
        return cachePopular;
    }

    @Override
    public Video getVideo(String videoId) {
        Video video = cacheAll.get(videoId);
        if (video == null) {
            video = thirdPartyYouTubeLib.getVideo(videoId);
            cacheAll.put(videoId, video);
        } else {
            LOGGER.info("从缓存中下载 {} 视频... ", videoId);
        }
        return video;
    }
}
