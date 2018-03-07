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
import cn.javaer.wechat.pay.WeChatPayRestTemplateClient;
import cn.javaer.wechat.pay.support.WeChatPayJaxb2RootElementHttpMessageConverter;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * @author zhangpeng
 */
public class WeChatPayClientFactory {
    public static final String OUT_TRADE_NO = "TEST02";
    private static WeChatPayClient weChatPayClient;

    public static void init() {
        WeChatPayConfigurator.DEFAULT.setAppid(System.getenv("wechat.pay.appid"));
        WeChatPayConfigurator.DEFAULT.setMchId(System.getenv("wechat.pay.mchId"));
        WeChatPayConfigurator.DEFAULT.setMchKey(System.getenv("wechat.pay.mchKey"));
        WeChatPayConfigurator.DEFAULT.setPayNotifyUrl(System.getenv("wechat.pay.payNotifyUrl"));
        WeChatPayConfigurator.DEFAULT.setApiBasePath("https://api.mch.weixin.qq.com");
        WeChatPayConfigurator.DEFAULT.setSpbillCreateIp("127.0.0.1");

        final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory;
        try {

            // 配置证书
            final KeyStore keystore = KeyStore.getInstance("PKCS12");
            final char[] partnerId2charArray = WeChatPayConfigurator.DEFAULT.getMchId().toCharArray();
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

            clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpclient);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        final RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.getMessageConverters().add(new WeChatPayJaxb2RootElementHttpMessageConverter());
        weChatPayClient = new WeChatPayRestTemplateClient(restTemplate);
    }

    public static WeChatPayClient client() {
        return weChatPayClient;
    }
}
