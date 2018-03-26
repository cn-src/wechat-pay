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

package cn.javaer.wechat.pay.spring;

import cn.javaer.wechat.pay.CientExecuteHook;
import cn.javaer.wechat.pay.WeChatPayConfigurator;
import cn.javaer.wechat.pay.WeChatPayService;
import cn.javaer.wechat.pay.client.HttpClientFactory;
import cn.javaer.wechat.pay.client.WeChatPayClient;
import cn.javaer.wechat.pay.client.WeChatPayHttpComponentsClient;
import cn.javaer.wechat.pay.model.UnifiedOrderRequest;
import cn.javaer.wechat.pay.model.UnifiedOrderResponse;
import cn.javaer.wechat.pay.spring.event.UnifiedOrderEvent;
import cn.javaer.wechat.pay.util.ObjectUtils;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * WeChatPayService 的 spring FactoryBean.
 *
 * @author zhangpeng
 */
public class WeChatPayServiceFactoryBean implements
        FactoryBean<WeChatPayService>, InitializingBean, ApplicationEventPublisherAware {

    private WeChatPayConfigurator weChatPayConfigurator;
    private WeChatPayService weChatPayService;
    private WeChatPayClient weChatPayClient;
    private Validator validator;
    private CientExecuteHook clientExecuteHook;
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public WeChatPayService getObject() throws Exception {
        return this.weChatPayService;
    }

    @Override
    public Class<?> getObjectType() {
        return WeChatPayService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ObjectUtils.checkNotNull(this.weChatPayConfigurator, "WeChatPayConfigurator");

        if (this.weChatPayClient == null) {
            this.weChatPayClient = weChatPayClient();
        }
        if (this.validator == null) {
            this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        }
        if (this.clientExecuteHook == null) {
            this.clientExecuteHook = (request, response) -> {
                if (request instanceof UnifiedOrderRequest) {
                    this.applicationEventPublisher.publishEvent(
                            new UnifiedOrderEvent((UnifiedOrderRequest) request,
                                    (UnifiedOrderResponse) response));
                }
            };
        }
        this.weChatPayService = new WeChatPayService(this.weChatPayClient, this.weChatPayConfigurator, this.validator);
        this.weChatPayService.setClientExecuteHook(this.clientExecuteHook);
    }

    private WeChatPayClient weChatPayClient() {
        // 避免硬编码对 org.apache.http.client.HttpClient 的依赖
        final HttpClient httpClient = new HttpClientFactory(
                this.weChatPayConfigurator.getMchId(), this.weChatPayConfigurator.getCertificatePath()).build();
        return new WeChatPayHttpComponentsClient(this.weChatPayConfigurator.getBasePath(), httpClient);
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setWeChatPayConfigurator(final WeChatPayConfigurator weChatPayConfigurator) {
        this.weChatPayConfigurator = weChatPayConfigurator;
    }

    public void setWeChatPayClient(final WeChatPayClient weChatPayClient) {
        this.weChatPayClient = weChatPayClient;
    }

    public void setValidator(final Validator validator) {
        this.validator = validator;
    }

    public void setClientExecuteHook(final CientExecuteHook clientExecuteHook) {
        this.clientExecuteHook = clientExecuteHook;
    }
}
