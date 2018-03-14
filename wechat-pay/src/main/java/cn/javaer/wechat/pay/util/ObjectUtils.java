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

package cn.javaer.wechat.pay.util;

import cn.javaer.wechat.pay.WeChatPayException;
import cn.javaer.wechat.pay.model.DownloadBillResponse;
import cn.javaer.wechat.pay.model.base.BasePayResponse;
import cn.javaer.wechat.pay.model.base.BillItem;
import cn.javaer.wechat.pay.model.base.Coupon;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author zhangpeng
 */
public class ObjectUtils {

    /**
     * 校验字符串不能为空.
     *
     * @param collection the collection
     * @param argumentName 参数名
     */
    public static void checkNotEmpty(final Collection<?> collection, final String argumentName) {
        if (null == collection || collection.isEmpty()) {
            throw new IllegalArgumentException("'" + argumentName + "' must be not empty");
        }
    }

    /**
     * 校验对象不能为 null.
     *
     * @param obj the obj
     * @param argumentName 参数名
     */
    public static void checkNotNull(final Object obj, final String argumentName) {
        if (null == obj) {
            throw new IllegalArgumentException("'" + argumentName + "' must be not null");
        }
    }

    /**
     * 校验字符串不能为空.
     *
     * @param str the str
     * @param argumentName 参数名
     */
    public static void checkNotEmpty(final String str, final String argumentName) {
        if (null == str || str.isEmpty()) {
            throw new IllegalArgumentException("'" + argumentName + "' must be not empty");
        }
    }

    /**
     * Gets all fields of the given class and its parents (if any) that are annotated with the given annotation.
     *
     * @param clazz the {@link Class} to query
     * @param annotationCls the {@link Annotation} that must be present on a field to be matched
     *
     * @return a list of Fields (possibly empty).
     *
     * @throws IllegalArgumentException if the class or annotation are {@code null}
     * @since 3.4
     */
    public static List<Field> getFieldsListWithAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotationCls) {
        checkNotNull(clazz, "The class must not be null");
        checkNotNull(annotationCls, "The annotation class must not be null");

        final List<Field> annotatedFields = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                if (field.getAnnotation(annotationCls) != null) {
                    field.setAccessible(true);
                    annotatedFields.add(field);
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return annotatedFields;
    }

    /**
     * 生成长度为 32 的 uuid.
     *
     * @return uuid32 String
     */
    public static String uuid32() {
        final StringBuilder sb = new StringBuilder(UUID.randomUUID().toString());
        sb.deleteCharAt(8);
        sb.deleteCharAt(12);
        sb.deleteCharAt(16);
        sb.deleteCharAt(20);
        return sb.toString();
    }

    /**
     * 获取完整的 api url.
     *
     * @param apiPath 微信 api path
     *
     * @return 拼接后的 url.
     */
    public static String fullApiUrl(final String basePath, final String apiPath) {
        checkNotEmpty(basePath, "basePath");
        checkNotEmpty(apiPath, "apiPath");

        final String tmp1 = basePath.endsWith("/") ? basePath.substring(0, basePath.length() - 1) : basePath;
        final String tmp2 = apiPath.startsWith("/") ? (tmp1 + apiPath) : (tmp1 + "/" + apiPath);
        return tmp2.endsWith("/") ? tmp2.substring(0, tmp2.length() - 1) : tmp2;
    }

    /**
     * 校验响应信息是否为成功.
     *
     * @param response BasePayResponse
     *
     * @throws WeChatPayException 没有响应信息, 响应信息标示不成功时抛出此异常.
     */
    public static void checkSuccessful(final BasePayResponse response) {
        if (!BasePayResponse.SUCCESS.equals(response.getReturnCode())) {
            throw new WeChatPayException("WeChat pay response 'return_code' is '" + response.getReturnCode()
                    + "', response:" + response.toString());
        }
        if (!BasePayResponse.SUCCESS.equals(response.getResultCode())) {
            throw new WeChatPayException("WeChat pay response 'result_code' is '" + response.getResultCode()
                    + "', response:" + response.toString());
        }
    }

    /**
     * 校验响应信息的签名.
     *
     * @param response BasePayResponse
     *
     * @throws WeChatPayException 签名错误时抛出此异常.
     */
    public static void checkSign(final BasePayResponse response, final String mchKey) {
        if (!response.getSign().equals(SignUtils.generateSign(response, mchKey))) {
            throw new WeChatPayException("WeChat pay response 'sign' error, response:" + response.toString());
        }
    }

    /**
     * 判断响应信息是否为成功.
     *
     * @param response BasePayResponse
     *
     * @return 有响应信息, 并且完全成功返回 true
     */
    public static boolean isSuccessful(final BasePayResponse response, final String mchKey) {

        return response.getSign().equals(SignUtils.generateSign(response, mchKey))
                && (BasePayResponse.SUCCESS.equals(response.getReturnCode()))
                && (BasePayResponse.SUCCESS.equals(response.getResultCode()));
    }


    /**
     * 动态数据的映射转换.
     *
     * <p>针对如: coupon_id_$n, coupon_type_$n, coupon_fee_$n 等.
     *
     * <h3>样例:</h3>
     * <pre>
     * final Map&lt;String, BiConsumer&lt;String, Coupon&gt;&gt; mapping = new HashMap&lt;&gt;();
     * mapping.put("coupon_id_", (val, coupon) -> coupon.setId(val));
     * mapping.put("coupon_type_", (val, coupon) -> coupon.setType(val));
     * mapping.put("coupon_fee_", (val, coupon) -> coupon.setFee(Integer.valueOf(val)));
     * ObjectUtils.beansMapFrom(this.otherParams, mapping, Coupon::new);
     * </pre>
     *
     * @param params 已存放的动态数据
     * @param mapping 转换函数的Map, 每一个 entry 的 key 为不带数字部分的前缀, 如 'coupon_id_'.
     *         value 为转换函数 BiConsumer&lt;V, T&gt; V 为 otherParams 的 value.
     * @param newT 新对象的创建函数
     * @param <T> 要转换的目标对象的类型
     *
     * @return 转换后的 Map, key 为 末尾数字, value 为转换后的对象.
     */
    public static <T> Map<String, T> beansMapFrom(
            final SortedMap<String, String> params,
            final Map<String, BiConsumer<String, T>> mapping,
            final Supplier<T> newT) {

        final Map<String, T> rtMap = new HashMap<>();
        for (final Map.Entry<String, String> entry : params.entrySet()) {

            final String key = entry.getKey();
            final String value = entry.getValue();
            if (null == value || value.isEmpty()) {
                continue;
            }
            for (final Map.Entry<String, BiConsumer<String, T>> mappingEntry : mapping.entrySet()) {
                final String keyStart = mappingEntry.getKey();
                if (key.matches(keyStart + "\\d+")) {
                    final String rtKey = key.substring(keyStart.length());
                    final T t = rtMap.computeIfAbsent(rtKey, k -> newT.get());
                    mappingEntry.getValue().accept(value, t);
                }
            }

        }

        return rtMap;
    }

    public static <T> List<T> beansFrom(
            final SortedMap<String, String> params,
            final Map<String, BiConsumer<String, T>> mapping,
            final Supplier<T> newT) {

        return new ArrayList<>(beansMapFrom(params, mapping, newT).values());
    }

    /**
     * 提取转换代金券信息.
     *
     * @param params params
     *
     * @return <code>Map&lt;String, Coupon&gt;</code>
     */
    public static List<Coupon> couponsFrom(final SortedMap<String, String> params) {
        final Map<String, BiConsumer<String, Coupon>> mappingMap = new HashMap<>(3);
        mappingMap.put("coupon_id_", (val, coupon) -> coupon.setId(val));
        mappingMap.put("coupon_type_", (val, coupon) -> coupon.setType(Coupon.Type.valueOf(val)));
        mappingMap.put("coupon_fee_", (val, coupon) -> coupon.setFee(Integer.valueOf(val)));

        return beansFrom(params, mappingMap, Coupon::new);
    }

    public static DownloadBillResponse billResponseFrom(final String str) {
        final String[] lines = str.split("\n");
        final String[] columns = lines[0].split(",");
        final List<BillItem> items = new ArrayList<>(lines.length - 3);
        for (int i = 1; i < lines.length - 2; i++) {
            final BillItem item = new BillItem();
            items.add(item);
            final String[] cells = lines[i].substring(1).split(",`");
            for (int j = 0; j < columns.length; j++) {
                setItem(item, columns[j], cells[j]);
            }
        }

        final DownloadBillResponse response = new DownloadBillResponse();
        final String[] countColumns = lines[lines.length - 2].split(",");
        for (int i = 0; i < countColumns.length; i++) {
            final String[] cells = lines[lines.length - 1].substring(1).split(",`");
            set(response, countColumns[i], cells[i]);
        }
        response.setBillItems(items);
        return response;
    }

    private static void set(final DownloadBillResponse response, final String columnName, final String value) {
        switch (columnName) {
            case "总交易单数":
                response.setTotalRecord(Integer.valueOf(value));
                break;
            case "总交易额":
                response.setTotalFee(value);
                break;
            case "总退款金额":
                response.setTotalRefundFee(value);
                break;
            case "总企业红包退款金额":
                response.setTotalCouponFee(value);
                break;
            case "手续费总金额":
                response.setTotalPoundageFee(value);
                break;
            default:
        }
    }

    private static void setItem(final BillItem item, final String columnName, final String value) {
        switch (columnName) {
            case "交易时间":
                item.setTradeTime(value);
                break;
            case "公众账号ID":
                item.setAppId(value);
                break;
            case "商户号":
                item.setMchId(value);
                break;
            case "子商户号":
                item.setSubMchId(value);
                break;
            case "设备号":
                item.setDeviceInfo(value);
                break;
            case "微信订单号":
                item.setTransactionId(value);
                break;
            case "商户订单号":
                item.setOutTradeNo(value);
                break;
            case "用户标识":
                item.setOpenId(value);
                break;
            case "交易类型":
                item.setTradeType(value);
                break;
            case "交易状态":
                item.setTradeState(value);
                break;
            case "付款银行":
                item.setBankType(value);
                break;
            case "货币种类":
                item.setFeeType(value);
                break;
            case "总金额":
                item.setTotalFee(value);
                break;
            case "企业红包金额":
                item.setCouponFee(value);
                break;
            case "微信退款单号":
                item.setRefundId(value);
                break;
            case "商户退款单号":
                item.setOutRefundNo(value);
                break;
            case "退款金额":
                item.setSettlementRefundFee(value);
                break;
            case "企业红包退款金额":
                item.setCouponRefundFee(value);
                break;
            case "退款类型":
                item.setRefundChannel(value);
                break;
            case "退款状态":
                item.setRefundState(value);
                break;
            case "商品名称":
                item.setBody(value);
                break;
            case "商户数据包":
                item.setAttach(value);
                break;
            case "手续费":
                item.setPoundage(value);
                break;
            case "费率":
                item.setPoundageRate(value);
                break;
            default:
        }
    }

}
