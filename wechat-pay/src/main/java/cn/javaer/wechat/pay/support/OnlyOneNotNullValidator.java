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

import cn.javaer.wechat.pay.UncheckedException;
import cn.javaer.wechat.pay.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link OnlyOneNotNull} 校验器.
 *
 * @author zhangpeng
 */
public class OnlyOneNotNullValidator implements ConstraintValidator<OnlyOneNotNull, Object> {

    private static final Map<Class, List<Field>> CACHE_FOR_SIGN = new ConcurrentHashMap<>();

    @Override
    public void initialize(final OnlyOneNotNull constraintAnnotation) {
        // nothing
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {

        final List<Field> fields = CACHE_FOR_SIGN.computeIfAbsent(value.getClass(), clazz0 ->
                ObjectUtils.getFieldsListWithAnnotation(clazz0, OnlyOne.class));

        if (null == fields || fields.isEmpty()) {
            return true;
        }
        int count = 0;
        try {
            for (final Field field : fields) {
                if (null != field.get(value)) {
                    count++;
                }

            }
        }
        catch (final IllegalAccessException e) {
            throw new UncheckedException(e);
        }

        return count == 1;
    }

}
