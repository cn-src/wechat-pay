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

import cn.javaer.wechat.pay.support.AnyElementsDomHandler;
import cn.javaer.wechat.pay.support.SignIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.SortedMap;

/**
 * 微信支付-基本响应信息.
 *
 * @author zhangpeng
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BasePayResponse {
    public static final String SUCCESS = "SUCCESS";
    @XmlAnyElement(AnyElementsDomHandler.class)
    @Setter(AccessLevel.PACKAGE)
    @SignIgnore
    protected SortedMap<String, String> otherParams;
    @XmlElement(name = "return_code")
    private String returnCode;
    @XmlElement(name = "return_msg")
    private String returnMsg;
    @XmlElement(name = "appid")
    private String appid;
    @XmlElement(name = "mch_id")
    private String mchId;
    @XmlElement(name = "sub_appid")
    private String subAppid;
    @XmlElement(name = "sub_mch_id")
    private String subMchId;
    @XmlElement(name = "nonce_str")
    private String nonceStr;
    @SignIgnore
    @XmlElement(name = "sign")
    private String sign;
    @XmlElement(name = "sign_type")
    private String signType;
    @XmlElement(name = "result_code")
    private String resultCode;
    @XmlElement(name = "err_code")
    private String errCode;
    @XmlElement(name = "err_code_des")
    private String errCodeDes;

    /**
     * 签名之前的处理, 子类可覆盖实现完成各自特定处理.
     */
    public void beforeSign() {}

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
