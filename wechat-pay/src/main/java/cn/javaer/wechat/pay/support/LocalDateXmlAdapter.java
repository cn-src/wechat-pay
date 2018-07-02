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

package cn.javaer.wechat.pay.support;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The type LocalDateTimeXmlAdapter.
 *
 * @author cn-src
 */
public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate> {
    public static final LocalDateXmlAdapter INSTANCE = new LocalDateXmlAdapter();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public LocalDate unmarshal(final String value) {
        if (null == value) {
            return null;
        }
        else {
            return LocalDate.parse(value, this.dateTimeFormatter);
        }
    }

    @Override
    public String marshal(final LocalDate value) {
        if (null == value) {
            return null;
        }
        else {
            return value.format(this.dateTimeFormatter);
        }
    }
}
