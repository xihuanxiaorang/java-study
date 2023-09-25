package fun.xiaorang.designpattern.proxy.jdk;

import fun.xiaorang.designpattern.proxy.statics.ThirdPartyYouTubeLib;
import fun.xiaorang.designpattern.proxy.statics.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">
 * <p>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a> @Copyright
 * 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a> - show me the code
 * @date 2023/5/11 12:51
 */
public class JdkYouTubeCacheProxy implements InvocationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdkYouTubeCacheProxy.class);
    private static final Map<String, Video> CACHE_ALL = new HashMap<>();
    private static Map<String, Video> cachePopular = new HashMap<>();
    private ThirdPartyYouTubeLib thirdPartyYouTubeLib;

    public ThirdPartyYouTubeLib getProxy(ThirdPartyYouTubeLib thirdPartyYouTubeLib) {
        this.thirdPartyYouTubeLib = thirdPartyYouTubeLib;
        return (ThirdPartyYouTubeLib)
                Proxy.newProxyInstance(
                        thirdPartyYouTubeLib.getClass().getClassLoader(),
                        thirdPartyYouTubeLib.getClass().getInterfaces(),
                        this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if ("popularVideos".equals(methodName)) {
            if (cachePopular.isEmpty()) {
                cachePopular = (Map<String, Video>) method.invoke(thirdPartyYouTubeLib, args);
            } else {
                LOGGER.info("从缓存中下载热门视频");
            }
            return cachePopular;
        } else if ("getVideo".equals(methodName)) {
            String videoId = (String) args[0];
            Video video = CACHE_ALL.get(videoId);
            if (video == null) {
                video = (Video) method.invoke(thirdPartyYouTubeLib, args);
                CACHE_ALL.put(videoId, video);
            } else {
                LOGGER.info("从缓存中下载 {} 视频... ", videoId);
            }
            return video;
        }
        return method.invoke(thirdPartyYouTubeLib, args);
    }
}
