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

package sample;

import cn.javaer.wechat.pay.WeChatPayClient;
import cn.javaer.wechat.pay.WeChatPayConfigurator;
import cn.javaer.wechat.pay.WeChatPayHttpComponentsClient;
import cn.javaer.wechat.pay.WeChatPayService;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.SSLContext;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * @author zhangpeng
 */
public class WeChatPayServiceFactory {
    private static WeChatPayService weChatPayService;

    public static void init() {
        final WeChatPayConfigurator configurator = new WeChatPayConfigurator();
        configurator.setAppid(System.getenv("wechat.pay.appid"));
        configurator.setMchId(System.getenv("wechat.pay.mchId"));
        configurator.setMchKey(System.getenv("wechat.pay.mchKey"));
        configurator.setNotifyUrl(System.getenv("wechat.pay.notifyUrl"));
        configurator.setBasePath("https://api.mch.weixin.qq.com");
//        configurator.setSpbillCreateIp("127.0.0.1");

        final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory;
        try {

            // 配置证书
            final KeyStore keystore = KeyStore.getInstance("PKCS12");
            final char[] partnerId2charArray = configurator.getMchId().toCharArray();
            keystore.load(new FileInputStream(System.getenv("wechat.pay.certificatePath")), partnerId2charArray);

            // ssl
            final SSLContext sslContext = SSLContexts.custom()
                    .loadKeyMaterial(keystore, partnerId2charArray).build();
            final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                    new String[]{"TLSv1"}, null, new DefaultHostnameVerifier());

            // httpclient
            final CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            final WeChatPayClient weChatPayClient = new WeChatPayHttpComponentsClient(httpclient);
            final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            weChatPayService = new WeChatPayService(weChatPayClient, configurator, validator);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static WeChatPayService weChatPayService() {
        return weChatPayService;
    }
}
