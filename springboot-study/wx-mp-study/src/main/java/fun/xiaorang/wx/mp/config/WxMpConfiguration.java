package fun.xiaorang.wx.mp.config;

import fun.xiaorang.wx.mp.handler.WxMpScanHandler;
import fun.xiaorang.wx.mp.handler.WxMpSubscribeHandler;
import fun.xiaorang.wx.mp.handler.WxMpUnSubscribeHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static me.chanjar.weixin.common.api.WxConsts.EventType.SCAN;
import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.EventType.UNSUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/19 16:10
 */
@Configuration
public class WxMpConfiguration {
    private final WxMpSubscribeHandler subscribeHandler;
    private final WxMpUnSubscribeHandler unSubscribeHandler;
    private final WxMpScanHandler scanHandler;

    public WxMpConfiguration(WxMpSubscribeHandler subscribeHandler, WxMpUnSubscribeHandler unSubscribeHandler, WxMpScanHandler scanHandler) {
        this.subscribeHandler = subscribeHandler;
        this.unSubscribeHandler = unSubscribeHandler;
        this.scanHandler = scanHandler;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        router.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(subscribeHandler).end();
        router.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(unSubscribeHandler).end();
        router.rule().async(false).msgType(EVENT).event(SCAN).handler(scanHandler).end();
        return router;
    }
}
