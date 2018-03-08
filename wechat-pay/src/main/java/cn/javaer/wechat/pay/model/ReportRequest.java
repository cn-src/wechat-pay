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
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付-交易保障-请求.
 *
 * @author zhangpeng
 */
@Getter
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class ReportRequest extends BasePayRequest {

    /**
     * 设备号.
     */
    @XmlElement(name = "device_info")
    private String deviceInfo;

    /**
     * 接口 URL.
     * <p>样例: https://api.mch.weixin.qq.com/pay/unifiedorder</p>
     */
    @XmlElement(name = "interface_url")
    private String interfaceUrl;

    /**
     * 接口耗时情况，单位为毫秒.
     */
    @XmlElement(name = "execute_time")
    private int executeTime;

    /**
     * 接口返回状态码.
     */
    @XmlElement(name = "return_code")
    private String returnCode;

    /**
     * 接口返回信息.
     */
    @XmlElement(name = "return_msg")
    private String returnMsg;

    /**
     * 接口返回业务结果.
     */
    @XmlElement(name = "result_code")
    private String resultCode;

    /**
     * 接口返回错误代码.
     */
    @XmlElement(name = "err_code")
    private String errCode;

    /**
     * 接口返回错误代码描述.
     */
    @XmlElement(name = "err_code_des")
    private String errCodeDes;

    /**
     * 商户订单号.
     */
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 访问接口IP.
     */
    @XmlElement(name = "user_ip")
    private String userIp;
    /**
     * 商户上报时间.
     */
    @XmlElement(name = "time")
    private String time;

}
