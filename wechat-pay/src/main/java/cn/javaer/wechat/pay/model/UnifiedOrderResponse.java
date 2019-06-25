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

package cn.javaer.wechat.pay.model;

import cn.javaer.wechat.pay.model.base.BasePayResponse;
import cn.javaer.wechat.pay.model.base.TradeType;
import cn.javaer.wechat.pay.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付-统一下单-响应.
 *
 * @author cn-src
 */
@Getter
@Setter
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class UnifiedOrderResponse extends BasePayResponse {

    @XmlElement(name = "device_info")
    private String deviceInfo;

    private TradeType tradeType;

    @XmlElement(name = "prepay_id")
    private String prepayId;

    @XmlElement(name = "code_url")
    private String codeUrl;

    @XmlElement(name = "mweb_url")
    private String mwebUrl;

    @Override
    protected void subProcessResponse() {
        this.tradeType = ObjectUtils.enumOf("trade_type", TradeType.class, this.otherParams);
    }
}
