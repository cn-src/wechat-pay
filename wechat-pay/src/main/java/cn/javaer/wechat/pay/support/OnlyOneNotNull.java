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

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义 Validator 校验, 检查被 {@link OnlyOne}标注的多个属性只能一个有值.
 *
 * @author cn-src
 * @see OnlyOne
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {OnlyOneNotNullValidator.class})
@Documented
public @interface OnlyOneNotNull {

    /**
     * 错误消息模板.
     *
     * @return message
     */
    String message() default "#default";

    /**
     * 校验组.
     *
     * @return groups
     */
    Class<?>[] groups() default {};

    /**
     * payload.
     *
     * @return payload
     */
    Class<? extends Payload>[] payload() default {};
}
