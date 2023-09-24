package fun.xiaorang.designpattern.proxy;

import fun.xiaorang.designpattern.proxy.cglib.CglibYouTubeCacheProxy;
import fun.xiaorang.designpattern.proxy.jdk.JdkYouTubeCacheProxy;
import fun.xiaorang.designpattern.proxy.statics.ThirdPartyYouTubeLib;
import fun.xiaorang.designpattern.proxy.statics.ThirdPartyYouTubeLibClass;
import fun.xiaorang.designpattern.proxy.statics.YouTubeCacheProxy;
import fun.xiaorang.designpattern.proxy.statics.YouTubeDownloader;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static net.sf.cglib.core.DebuggingClassWriter.DEBUG_LOCATION_PROPERTY;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">
 * <p>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a> @Copyright
 * 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a> - show me the code
 * @date 2023/5/10 1:43
 */
class ApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTest.class);

    public static void main(String[] args) {
//        test_01();
        test_02();
    }

    public static void test_01() {
        ThirdPartyYouTubeLib thirdPartyYouTubeLib = new ThirdPartyYouTubeLibClass();
        ThirdPartyYouTubeLib proxy = new JdkYouTubeCacheProxy().getProxy(thirdPartyYouTubeLib);
        YouTubeDownloader naiveDownloader = new YouTubeDownloader(thirdPartyYouTubeLib);
        YouTubeDownloader smartDownloader = new YouTubeDownloader(proxy);

        long naive = test(naiveDownloader);
        LOGGER.info(
                "--------------------------------------------------------------------------------\n");
        long smart = test(smartDownloader);
        LOGGER.info("通过缓存代理可以节约 {} ms", (naive - smart));
    }

    public static long test(YouTubeDownloader downloader) {
        long startTime = System.currentTimeMillis();

        // 应用中的用户行为如下：
        downloader.renderPopularVideos();
        downloader.renderVideoPage("catzzzzzzzzz");
        downloader.renderPopularVideos();
        downloader.renderVideoPage("dancesvideoo");
        // 用户有可能会经常访问同一个页面
        downloader.renderVideoPage("catzzzzzzzzz");
        downloader.renderVideoPage("someothervid");

        long estimatedTime = System.currentTimeMillis() - startTime;
        LOGGER.info("测试总共耗时 {} ms", estimatedTime);
        return estimatedTime;
    }

    public static void test_02() {
        System.setProperty(DEBUG_LOCATION_PROPERTY, new File("").getAbsolutePath() + "/cglib");
        ThirdPartyYouTubeLibClass thirdPartyYouTubeLib = new ThirdPartyYouTubeLibClass();
        ThirdPartyYouTubeLib proxy = new CglibYouTubeCacheProxy().getProxy(thirdPartyYouTubeLib);
        YouTubeDownloader naiveDownloader = new YouTubeDownloader(thirdPartyYouTubeLib);
        YouTubeDownloader smartDownloader = new YouTubeDownloader(proxy);

        long naive = test(naiveDownloader);
        LOGGER.info(
                "--------------------------------------------------------------------------------\n");
        long smart = test(smartDownloader);
        LOGGER.info("通过缓存代理可以节约 {} ms", (naive - smart));
    }

    @Test
    public void test_00() {
        ThirdPartyYouTubeLib thirdPartyYouTubeLib = new ThirdPartyYouTubeLibClass();
        YouTubeDownloader naiveDownloader = new YouTubeDownloader(thirdPartyYouTubeLib);
        YouTubeDownloader smartDownloader =
                new YouTubeDownloader(new YouTubeCacheProxy(thirdPartyYouTubeLib));

        long naive = test(naiveDownloader);
        LOGGER.info(
                "--------------------------------------------------------------------------------\n");
        long smart = test(smartDownloader);
        LOGGER.info("通过缓存代理可以节约 {} ms", (naive - smart));
    }
}
