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
import cn.javaer.wechat.pay.model.base.BillType;
import cn.javaer.wechat.pay.model.base.TarType;
import cn.javaer.wechat.pay.support.LocalDateXmlAdapter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

/**
 * 微信支付-下载对账单-请求.
 *
 * @author zhangpeng
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class DownloadBillRequest extends BasePayRequest {

    /**
     * 对账单日期.
     *
     * <p>下载对账单的日期，格式：20140603</p>
     */
    @XmlElement(name = "bill_date")
    @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
    private LocalDate billDate;

    /**
     * 账单类型.
     */
    @NonNull
    @XmlElement(name = "bill_type")
    private BillType billType;

    /**
     * 压缩账单.
     *
     * <p>非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。</p>
     */
    @XmlElement(name = "tar_type")
    private TarType tarType;

}
