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

package cn.javaer.wechat.pay.starter;

import cn.javaer.wechat.pay.WeChatPayService;
import cn.javaer.wechat.pay.client.HttpClientFactory;
import cn.javaer.wechat.pay.client.WeChatPayClient;
import cn.javaer.wechat.pay.client.WeChatPayHttpComponentsClient;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;


/**
 * 微信支付自动配置.
 *
 * @author zhangpeng
 */
@Configuration
@ConditionalOnClass(WeChatPayClient.class)
@ConditionalOnWebApplication
@AutoConfigureAfter({WebClientAutoConfiguration.class})
@EnableConfigurationProperties(WeChatPayProperties.class)
public class WeChatPayAutoConfiguration {
    private final WeChatPayProperties weChatPayProperties;

    public WeChatPayAutoConfiguration(final WeChatPayProperties weChatPayProperties) {
        this.weChatPayProperties = weChatPayProperties;
    }

    /**
     * WeChatPayClient 配置.
     *
     * @return WeChatPayClient
     */
    @Bean
    @ConditionalOnMissingBean
    public WeChatPayClient weChatPayClient() {
        final HttpClient httpClient = new HttpClientFactory(
                this.weChatPayProperties.getMchId(), this.weChatPayProperties.getCertificatePath()).build();
        return new WeChatPayHttpComponentsClient(this.weChatPayProperties.getBasePath(), httpClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeChatPayService weChatPayService() {
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        return new WeChatPayService(weChatPayClient(), this.weChatPayProperties, validator);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeChatPayController weChatPayController(final ApplicationEventPublisher publisher) {
        return new WeChatPayController(publisher);
    }
}
