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

package cn.javaer.wechat.pay.model.base;

import cn.javaer.wechat.pay.support.SignIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 微信支付-基本请求信息.
 *
 * @author cn-src
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BasePayRequest {
    /**
     * 公众账号ID.
     */
    @NotEmpty
    @XmlElement(name = "appid")
    private String appId;

    /**
     * 商户号.
     */
    @NotEmpty
    @XmlElement(name = "mch_id")
    private String mchId;

    /**
     * 随机字符串.
     */
    @NotNull
    @Length(min = 1, max = 32)
    @XmlElement(name = "nonce_str")
    private String nonceStr;

    /**
     * 签名.
     */
    @NotEmpty
    @SignIgnore
    @XmlElement(name = "sign")
    private String sign;

    /**
     * 签名类型.
     */
    @XmlElement(name = "sign_type")
    private SignType signType;

}
