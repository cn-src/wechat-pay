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

import cn.javaer.wechat.pay.model.base.BillItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 微信支付-下载对账单-请求.
 *
 * @author zhangpeng
 */
@Getter
@Setter
public class DownloadBillResponse {

    private List<BillItem> billItems;
    /**
     * 总交易单数
     */
    private Integer totalRecord;
    /**
     * 总交易额
     */
    private String totalFee;
    /**
     * 总退款金额
     */
    private String totalRefundFee;
    /**
     * 总代金券或立减优惠退款金额
     */
    private String totalCouponFee;
    /**
     * 手续费总金额
     */
    private String totalPoundageFee;
}
