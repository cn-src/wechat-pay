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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 支付类型的下单参数规则自定义校验器.
 *
 * @author zhangpeng
 */
public class ValidTradeTypeRuleValidator implements ConstraintValidator<ValidTradeTypeRule, UnifiedOrderRequest> {
    @Override
    public void initialize(final ValidTradeTypeRule constraintAnnotation) {
        // nothing
    }

    @Override
    public boolean isValid(final UnifiedOrderRequest value, final ConstraintValidatorContext context) {
        if (TradeType.NATIVE.equals(value.getTradeType())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("TradeType is NATIVE, productId must be not null");
            return null != value.getProductId();
        }
        if (TradeType.JSAPI.equals(value.getTradeType())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("TradeType is JSAPI, openid must be not null");
            return null != value.getOpenId();
        }
        return false;
    }
}
