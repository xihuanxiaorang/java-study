package fun.xiaorang.designpattern.proxy.statics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">YouTube远程服务实现
 * <p>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a> @Copyright
 * 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a> - show me the code
 * @date 2023/5/10 15:10
 */
public class ThirdPartyYouTubeLibClass implements ThirdPartyYouTubeLib {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyYouTubeLibClass.class);

    @Override
    public Map<String, Video> popularVideos() {
        LOGGER.info("-------------------------------");
        connectToServer("https://www.youtube.com");
        return getRandomVideos();
    }

    @Override
    public Video getVideo(String videoId) {
        LOGGER.info("-------------------------------");
        connectToServer("https://www.youtube.com/" + videoId);
        return getSomeVideo(videoId);
    }

    private Map<String, Video> getRandomVideos() {
        LOGGER.info("开始下载热门视频 ... ");
        experienceNetworkLatency();
        Map<String, Video> videoMap = new HashMap<>(5);
        videoMap.put("catzzzzzzzzz", new Video("sadgahasgdas", "Catzzzz.avi"));
        videoMap.put("mkafksangasj", new Video("mkafksangasj", "Dog play with ball.mp4"));
        videoMap.put("dancesvideoo", new Video("asdfas3ffasd", "Dancing video.mpq"));
        videoMap.put("dlsdk5jfslaf", new Video("dlsdk5jfslaf", "Barcelona vs RealM.mov"));
        videoMap.put("3sdfgsd1j333", new Video("3sdfgsd1j333", "Programing lesson#1.avi"));
        LOGGER.info("Done!");
        return videoMap;
    }

    private Video getSomeVideo(String videoId) {
        LOGGER.info("开始下载 {} 视频... ", videoId);
        experienceNetworkLatency();
        Video video = new Video(videoId, "Some video title");
        LOGGER.info("Done!");
        return video;
    }

    private int random(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    /**
     * 模拟网络延迟
     */
    private void experienceNetworkLatency() {
        int randomLatency = random(5, 10);
        for (int i = 0; i < randomLatency; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void connectToServer(String server) {
        LOGGER.info("Connecting to {} ... ", server);
        experienceNetworkLatency();
        LOGGER.info("Connected!");
    }
}
