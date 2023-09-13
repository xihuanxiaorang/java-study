package fun.xiaorang.wx.mp.controller;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/20 3:18
 */
@RestController
@RequestMapping("/wx/mp")
public class WxMpController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxMpController.class);
    private final WxMpService wxMpService;

    public WxMpController(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    /**
     * 创建用户绑定微信公众号的临时二维码
     *
     * @param userNo 用户编号，用于创建二维码
     * @return 二维码图片的URL地址
     * @throws WxErrorException 如果创建二维码时出现错误，将抛出WxErrorException异常
     */
    @GetMapping("/qr-code/{userNo}")
    public String createQrCode(@PathVariable String userNo) throws WxErrorException {
        LOGGER.info("当前正在绑定微信公众号的用户编号为: {}", userNo);
        // 获取ticket，时间不填默认30秒，最大30天
        WxMpQrCodeTicket ticket = this.wxMpService.getQrcodeService().qrCodeCreateTmpTicket(userNo, null);
        // 根据ticket创建临时二维码
        return this.wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
    }

    /**
     * 处理微信OAuth2认证回调请求
     *
     * @param appid 微信应用程序的ID
     * @param code  授权码，用于获取访问令牌
     * @param state 用于验证请求的安全性
     * @return 微信OAuth2用户信息
     * @throws WxErrorException 如果处理时出现错误，将抛出WxErrorException异常
     */
    @GetMapping("/{appid}/callback")
    public WxOAuth2UserInfo callback(@PathVariable String appid, @RequestParam String code, @RequestParam String state) throws WxErrorException {
        LOGGER.info("\n接收微信请求：[appid=[{}], code=[{}], state=[{}]]", appid, code, state);
        if (!Objects.equals(state, wxMpService.getWxMpConfigStorage().getToken())) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        WxOAuth2Service oAuth2Service = wxMpService.getOAuth2Service();
        // 利用code获取accessToken
        WxOAuth2AccessToken accessToken = oAuth2Service.getAccessToken(code);
        // 利用accessToken获取用户信息
        return oAuth2Service.getUserInfo(accessToken, null);
    }

    /**
     * 发送模板消息
     *
     * @return 发送模板消息的结果，包括消息ID或错误信息
     * @throws WxErrorException 如果发送模板消息时出现错误，将抛出WxErrorException异常
     */
    @GetMapping("/template-message/send")
    public String sendTemplateMessage() throws WxErrorException {
        LOGGER.info(wxMpService.getAccessToken());
        // 创建模板消息对象
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                // 接收者openid
                .toUser("oQeyd6s9Sn8Zp9-OmfZwxgCBEK0E")
                // 模板id
                .templateId("NsKCBC-lCaippbIiD8P-_mY2q3Ul29Sald36IBqHIv8")
                // 模板跳转链接
                .url("https://www.baidu.com").build();
        // 添加模板数据
        templateMessage.addData(new WxMpTemplateData("first", "用餐愉快哦", "#FF00FF"))
                .addData(new WxMpTemplateData("keyword1", "微信点餐", "#A9A9A9"))
                .addData(new WxMpTemplateData("keyword2", "13826913333", "#FF00FF"))
                .addData(new WxMpTemplateData("keyword3", "2021081722150001", "#FF00FF"))
                .addData(new WxMpTemplateData("keyword4", "￥56.5", "#FF00FF"))
                .addData(new WxMpTemplateData("remark", "用餐愉快哦", "#000000"));
        String msgId = null;
        try {
            // 发送模板消息
            msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            LOGGER.info(wxMpService.getAccessToken());
            LOGGER.warn("·==++--·推送微信模板信息：{}·--++==·", "成功");
        } catch (WxErrorException e) {
            System.out.println(wxMpService.getAccessToken());
            LOGGER.warn("·==++--·推送微信模板信息：{}·--++==·", "失败");
            e.printStackTrace();
        }
        return msgId;
    }
}
