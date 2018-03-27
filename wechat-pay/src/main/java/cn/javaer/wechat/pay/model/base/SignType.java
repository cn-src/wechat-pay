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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * 签名类型.
 *
 * @author zhangpeng
 */
@XmlType
@XmlEnum
public enum SignType {
    @XmlEnumValue("MD5") MD5("MD5"),
    @XmlEnumValue("HMAC-SHA256") HMAC_SHA256("HMAC-SHA256");
    private final String value;

    SignType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
