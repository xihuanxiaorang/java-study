package fun.xiaorang.designpattern.proxy.cglib;

import fun.xiaorang.designpattern.proxy.statics.ThirdPartyYouTubeLibClass;
import fun.xiaorang.designpattern.proxy.statics.Video;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">
 * <p>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a> @Copyright
 * 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a> - show me the code
 * @date 2023/5/11 15:11
 */
public class CglibYouTubeCacheProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(CglibYouTubeCacheProxy.class);
    private static final Map<String, Video> CACHE_ALL = new HashMap<>();
    private static Map<String, Video> cachePopular = new HashMap<>();

    public ThirdPartyYouTubeLibClass getProxy(ThirdPartyYouTubeLibClass thirdPartyYouTubeLib) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(thirdPartyYouTubeLib.getClass());
        enhancer.setInterfaces(thirdPartyYouTubeLib.getClass().getInterfaces());
        enhancer.setCallbacks(new Callback[]{
                (MethodInterceptor) (obj, method, args, methodProxy) -> {
                    if (cachePopular.isEmpty()) {
                        cachePopular = (Map<String, Video>) methodProxy.invokeSuper(obj, args);
                    } else {
                        LOGGER.info("从缓存中下载热门视频");
                    }
                    return cachePopular;
                },
                (MethodInterceptor) (obj, method, args, methodProxy) -> {
                    String videoId = (String) args[0];
                    Video video = CACHE_ALL.get(videoId);
                    if (video == null) {
                        video = (Video) methodProxy.invokeSuper(obj, args);
                        CACHE_ALL.put(videoId, video);
                    } else {
                        LOGGER.info("从缓存中下载 {} 视频... ", videoId);
                    }
                    return video;
                }
        });
        enhancer.setCallbackFilter(method -> {
            if ("popularVideos".equals(method.getName())) {
                return 0;
            } else {
                return 1;
            }
        });
        return (ThirdPartyYouTubeLibClass) enhancer.create();
    }
}
