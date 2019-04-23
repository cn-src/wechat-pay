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

import cn.javaer.wechat.pay.client.WeChatPayClient;
import cn.javaer.wechat.pay.spring.WeChatPayController;
import cn.javaer.wechat.pay.spring.WeChatPayServiceFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 微信支付自动配置.
 *
 * @author cn-src
 */
@Configuration
@ConditionalOnClass(WeChatPayClient.class)
@ConditionalOnWebApplication
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
@EnableConfigurationProperties(WeChatPayProperties.class)
public class WeChatPayAutoConfiguration {
    private final WeChatPayProperties weChatPayProperties;

    public WeChatPayAutoConfiguration(final WeChatPayProperties weChatPayProperties) {
        this.weChatPayProperties = weChatPayProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public WeChatPayServiceFactoryBean weChatPayServiceFactoryBean() {
        final WeChatPayServiceFactoryBean factoryBean = new WeChatPayServiceFactoryBean();
        factoryBean.setWeChatPayConfigurator(this.weChatPayProperties);
        return factoryBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public WeChatPayController weChatPayController() {
        return new WeChatPayController(this.weChatPayProperties.getMchKey());
    }
}
