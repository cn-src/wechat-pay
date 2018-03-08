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

import cn.javaer.wechat.pay.WeChatPayConfigurator;
import cn.javaer.wechat.pay.WeChatPayUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * JSAPI 支付需要的参数.
 *
 * @author zhangpeng
 */
@Data
public class JsParams {
    private String appId;
    private String timeStamp;
    private String nonceStr;
    @JsonProperty("package")
    private String packageStr;
    private String signType;
    private String paySign;

    private JsParams() {}

    public static JsParams create(final String prepayId) {
        final JsParams params = new JsParams();
        params.appId = WeChatPayConfigurator.DEFAULT.getAppid();
        params.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        params.nonceStr = WeChatPayUtils.uuid32();
        params.packageStr = "prepay_id=" + prepayId;
        params.signType = "MD5";
        params.paySign = "";  // TODO
        return params;
    }
}
