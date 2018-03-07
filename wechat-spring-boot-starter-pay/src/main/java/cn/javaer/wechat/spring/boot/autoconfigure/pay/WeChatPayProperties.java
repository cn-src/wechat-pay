/*
 * Copyright (c) 2018 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.javaer.wechat.spring.boot.autoconfigure.pay;

import cn.javaer.wechat.pay.WeChatPayClient;
import cn.javaer.wechat.pay.WeChatPayConfigurator;
import cn.javaer.wechat.pay.WeChatPayUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * 微信支付-配置.
 *
 * @author zhangpeng
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "wechat.pay")
public class WeChatPayProperties implements EnvironmentAware, InitializingBean {
    public static final String NOTIFY_RESULT_PATH = "/public/wechat/pay/notify_result";
    private Environment environment;


    /**
     * 公众号的唯一标识.
     */
    @NonNull
    private String appid;

    /**
     * 商户号.
     */
    @NonNull
    private String mchId;

    /**
     * 商户的key, 需要去微信支付平台设置.
     */
    @NonNull
    private String mchKey;

    /**
     * 接收微信异步通知消息的地址.
     * <br>
     * 如：http://www.demo.com:8080
     */
    @NonNull
    private String notifyAddress;

    /**
     * 服务器的公网ip.
     */
    @NonNull
    private String clientIp;

    /**
     * 接收支付结果通知 Controller 的 path.
     */
    private String notifyResultPath = NOTIFY_RESULT_PATH;

    private String apiBasePath = WeChatPayClient.BASE_PATH;

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final String mpAppId = this.environment.getProperty("wechat.mp.appid");
        if (!StringUtils.hasText(this.appid) && StringUtils.hasText(mpAppId)) {
            this.appid = mpAppId;
        }

        final String mpNotifyAddress = this.environment.getProperty("wechat.mp.notifyAddress");
        if (!StringUtils.hasText(this.notifyAddress) && StringUtils.hasText(mpNotifyAddress)) {
            this.notifyAddress = mpNotifyAddress;
        }

        WeChatPayConfigurator.DEFAULT.setAppid(this.appid);
        WeChatPayConfigurator.DEFAULT.setMchId(this.mchId);
        WeChatPayConfigurator.DEFAULT.setMchKey(this.mchKey);
        WeChatPayConfigurator.DEFAULT.setSpbillCreateIp(this.clientIp);
        WeChatPayConfigurator.DEFAULT.setPayNotifyUrl(WeChatPayUtils.joinPath(this.notifyAddress, this.notifyResultPath));
    }
}
