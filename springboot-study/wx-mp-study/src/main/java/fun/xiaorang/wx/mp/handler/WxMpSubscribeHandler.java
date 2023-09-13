package fun.xiaorang.wx.mp.handler;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.OAuth2Scope.SNSAPI_USERINFO;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/19 16:00
 */
@Component
public class WxMpSubscribeHandler implements WxMpMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxMpSubscribeHandler.class);

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        if (StringUtils.hasText(wxMessage.getEventKey())) {
            // 通过扫描带场景值二维码关注的用户，用于系统绑定用户
            LOGGER.info("用户编号为：{}", wxMessage.getEventKey().split("_")[1]);
        }
        LOGGER.info("新用户关注 OPENID: {}", wxMessage.getFromUser());
        String uri = "http://e3fnyv.natappfree.cc/wx/mp/APPID/callback";
        uri = uri.replace("APPID", wxMpService.getWxMpConfigStorage().getAppId());
        String href = "欢迎关注！<a href=\"" + wxMpService.getOAuth2Service().buildAuthorizationUrl(uri, SNSAPI_USERINFO, wxMpService.getWxMpConfigStorage().getToken()) + "\">请点击此处进行网页授权，测试用！！！</a>";
        return WxMpXmlOutMessage.TEXT().content(href).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
    }
}
