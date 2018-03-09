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

import cn.javaer.wechat.pay.WeChatPayUtils;
import cn.javaer.wechat.pay.support.SignType;
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
    private SignType signType;
    private String paySign;

    private JsParams() {}

    /**
     * 公众号支付时前端 js 所需要的参数.
     *
     * @param prepayId the prepay id
     * @param signType the sign type, signType 必须与下单的一致
     * @param appId the app id
     *
     * @return the js params
     */
    public static JsParams create(final String prepayId, final SignType signType, final String appId) {
        final JsParams params = new JsParams();
        params.appId = appId;
        params.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        params.nonceStr = WeChatPayUtils.uuid32();
        params.packageStr = "prepay_id=" + prepayId;
        params.signType = signType == null ? SignType.MD5 : signType;
        params.paySign = "";  // TODO
        return params;
    }
}
