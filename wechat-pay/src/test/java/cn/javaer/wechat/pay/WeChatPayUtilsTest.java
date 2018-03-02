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

package cn.javaer.wechat.pay;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author zhangpeng
 */
public class WeChatPayUtilsTest {

    @Test
    public void joinPath() {
        assertEquals("http://demo.com/demo", WeChatPayUtils.joinPath("http://demo.com", "/demo"));
        assertEquals("http://demo.com/demo", WeChatPayUtils.joinPath("http://demo.com", "/demo/"));
        assertEquals("http://demo.com/demo", WeChatPayUtils.joinPath("http://demo.com", "demo/"));
        assertEquals("http://demo.com/demo", WeChatPayUtils.joinPath("http://demo.com/", "/demo"));
        assertEquals("http://demo.com/demo", WeChatPayUtils.joinPath("http://demo.com/", "/demo/"));
        assertEquals("http://demo.com/demo", WeChatPayUtils.joinPath("http://demo.com/", "demo/"));
    }
}