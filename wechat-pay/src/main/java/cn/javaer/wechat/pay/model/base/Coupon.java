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

import lombok.Data;

/**
 * 代金券.
 *
 * @author cn-src
 */
@Data
public class Coupon {

    private String id;
    private Type type;
    private Integer fee;

    /**
     * 代金券类型.
     */
    public enum Type {
        /**
         * 充值代金券.
         */
        CASH,
        /**
         * 非充值代金券.
         */
        NO_CASH
    }
}