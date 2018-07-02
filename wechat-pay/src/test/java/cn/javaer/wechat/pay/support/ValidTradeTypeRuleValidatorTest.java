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

import cn.javaer.wechat.pay.model.UnifiedOrderRequest;
import cn.javaer.wechat.pay.model.base.TradeType;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
public class ValidTradeTypeRuleValidatorTest {

    @Test
    public void isValid() {
        final UnifiedOrderRequest request = new UnifiedOrderRequest();
        request.setAppId("x");
        request.setMchId("x");
        request.setNonceStr("x");
        request.setBody("x");
        request.setSign("x");
        request.setOutTradeNo("x");
        request.setTotalFee(3);
        request.setSpbillCreateIp("x");
        request.setNotifyUrl("http://host");
        request.setTradeType(TradeType.JSAPI);

        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<UnifiedOrderRequest>> violationSet = validator.validate(request);
        assertThat(violationSet).hasSize(1);
        final ConstraintViolation<UnifiedOrderRequest> constraintViolation = violationSet.iterator().next();
        assertThat(constraintViolation.getPropertyPath().toString()).isEqualTo("openId");
        assertThat(constraintViolation.getMessage()).isEqualTo("TradeType is JSAPI, openId must be not null");
    }
}