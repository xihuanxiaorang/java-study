package fun.xiaorang.wx.mp.controller;

import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/19 13:27
 */
@RestController
@RequestMapping("/wx/mp/portal/{appid}")
public class WxPortalController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxPortalController.class);
    private final WxMpService wxMpService;
    private final WxMpMessageRouter messageRouter;

    public WxPortalController(WxMpService wxMpService, WxMpMessageRouter messageRouter) {
        this.wxMpService = wxMpService;
        this.messageRouter = messageRouter;
    }

    /**
     * 处理微信服务器的认证消息
     *
     * @param appid     微信应用程序的ID
     * @param signature 请求的签名参数，用于认证消息的真实性
     * @param timestamp 请求的时间戳参数，用于认证消息的真实性
     * @param nonce     请求的随机数参数，用于认证消息的真实性
     * @param echostr   用于微信服务器的认证时的随机字符串
     * @return 如果认证成功，则返回echostr，否则返回"非法请求"
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(
            @PathVariable String appid,
            @RequestParam(name = "signature", required = false) String signature,
            @RequestParam(name = "timestamp", required = false) String timestamp,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "echostr", required = false) String echostr) {

        LOGGER.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (!this.wxMpService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }

    /**
     * 处理来自微信的消息
     *
     * @param appid        微信应用程序的ID
     * @param requestBody  包含微信消息的请求体
     * @param signature    用于验证的请求签名
     * @param timestamp    用于验证的请求时间戳
     * @param nonce        用于验证的随机nonce值
     * @param openid       微信用户的唯一标识符
     * @param encType      加密类型（可选，默认为null表示明文）
     * @param msgSignature 消息签名（可选，加密消息时需要）
     * @return 以XML格式的微信响应消息。
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String handleMessage(
            @PathVariable String appid,
            @RequestBody String requestBody,
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("openid") String openid,
            @RequestParam(name = "encrypt_type", required = false) String encType,
            @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        LOGGER.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}], timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                openid, signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!wxMpService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxMpService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
            LOGGER.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toEncryptedXml(wxMpService.getWxMpConfigStorage());
        }

        LOGGER.debug("\n组装回复信息：{}", out);
        return out;
    }

    /**
     * 根据接收到的微信XML消息进行路由处理
     *
     * @param message 接收到的微信XML消息
     * @return 微信XML响应消息，或者在出现异常时返回null
     */
    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            // 使用消息路由器处理接收到的消息
            return messageRouter.route(message);
        } catch (Exception e) {
            LOGGER.error("路由消息时出现异常！", e);
        }
        return null;
    }
}
