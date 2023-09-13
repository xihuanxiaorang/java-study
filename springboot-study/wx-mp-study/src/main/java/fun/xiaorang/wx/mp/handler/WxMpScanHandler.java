package fun.xiaorang.wx.mp.handler;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/20 3:38
 */
@Component
public class WxMpScanHandler implements WxMpMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxMpScanHandler.class);

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        LOGGER.info("系统用户账号为：{}", wxMessage.getEventKey());
        LOGGER.info("openId: {}", wxMessage.getFromUser());
        return WxMpXmlOutMessage.TEXT().content("绑定系统用户成功！").fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
    }
}
