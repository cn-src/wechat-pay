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

import cn.javaer.wechat.pay.model.base.BasePayRequest;
import cn.javaer.wechat.pay.model.base.TradeType;
import cn.javaer.wechat.pay.support.Groups;
import cn.javaer.wechat.pay.support.LocalDateTimeXmlAdapter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

/**
 * 微信支付-统一下单-请求.
 *
 * @author zhangpeng
 */
@EqualsAndHashCode(callSuper = true)
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class UnifiedOrderRequest extends BasePayRequest {
    /**
     * 设备号.
     */
    @Length(min = 1, max = 32)
    @XmlElement(name = "device_info")
    private String deviceInfo;
    /**
     * 商品描述.
     */
    @NotNull
    @Length(min = 1, max = 128)
    @XmlElement(name = "body")
    private String body;
    /**
     * 商品详情.
     */
    @Length(min = 1, max = 6000)
    @XmlElement(name = "detail")
    private String detail;
    /**
     * 附加数据.
     */
    @Length(min = 1, max = 127)
    @XmlElement(name = "attach")
    private String attach;
    /**
     * 商户订单号.
     */
    @NotNull
    @Length(min = 1, max = 32)
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 货币类型.
     */
    @XmlElement(name = "fee_type")
    private String feeType;
    /**
     * 总金额.
     */
    @NotNull
    @Min(1)
    @Max(10_0000_00)
    @XmlElement(name = "total_fee")
    private Integer totalFee;
    /**
     * 终端IP.
     */
    @NotEmpty
    @XmlElement(name = "spbill_create_ip")
    private String spbillCreateIp;
    /**
     * 交易起始时间.
     */
    @XmlElement(name = "time_start")
    @XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
    private LocalDateTime timeStart;
    /**
     * 交易结束时间.
     */
    @XmlElement(name = "time_expire")
    @XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
    private LocalDateTime timeExpire;
    /**
     * 订单优惠标记.
     */
    @Length(min = 1, max = 32)
    @XmlElement(name = "goods_tag")
    private String goodsTag;
    /**
     * 通知地址.
     */
    @NotNull
    @URL
    @XmlElement(name = "notify_url")
    private String notifyUrl;
    /**
     * 交易类型.
     */
    @NotNull
    @XmlElement(name = "trade_type")
    private TradeType tradeType;
    /**
     * 商品ID.
     */
    @NotNull(groups = Groups.NATIVE.class)
    @Length(min = 1, max = 32)
    @XmlElement(name = "product_id")
    private String productId;
    /**
     * 指定支付方式.
     */
    @XmlElement(name = "limit_pay")
    private String limitPay;
    /**
     * 用户标识.
     */
    @NotNull(groups = Groups.JSAPI.class)
    @Length(min = 1, max = 128)
    @XmlElement(name = "openid")
    private String openid;
    /**
     * 场景信息.
     */
    @Length(min = 1, max = 256)
    @XmlElement(name = "scene_info")
    private String sceneInfo;

}
