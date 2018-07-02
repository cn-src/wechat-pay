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

/**
 * 退款状态.
 *
 * @author cn-src
 */
public enum RefundStatus {

    /**
     * 退款成功.
     */
    SUCCESS,

    /**
     * 退款关闭.
     */
    REFUNDCLOSE,

    /**
     * 退款处理中.
     */
    PROCESSING,

    /**
     * 退款异常.
     */
    CHANGE
}
