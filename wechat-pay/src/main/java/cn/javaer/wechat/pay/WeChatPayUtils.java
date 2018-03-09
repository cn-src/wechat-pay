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

import cn.javaer.wechat.pay.model.base.BasePayRequest;
import cn.javaer.wechat.pay.model.base.BasePayResponse;
import cn.javaer.wechat.pay.model.base.Coupon;
import cn.javaer.wechat.pay.support.SignIgnore;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

/**
 * 微信支付工具类.
 *
 * @author zhangpeng
 */
public class WeChatPayUtils {

    private static final Map<Class, List<Field>> CACHE_FOR_SIGN = new ConcurrentHashMap<>();

    static {
        // 对加解密中 PKCS7Padding 模式的支持
        Security.addProvider(new BouncyCastleProvider());
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
     * 微信支付-生成签名.
     *
     * @param request 要签名的数据对象.
     * @param mchKey mchKey
     *
     * @return 返回签名 String
     */

    public static String generateSign(
            final BasePayRequest request, final String mchKey) {

        return generateSign(signParamsFrom(request), mchKey);
    }

    /**
     * 微信支付-生成签名.
     *
     * @param response 要签名的数据对象.
     * @param mchKey mchKey
     *
     * @return 返回签名 String
     */

    public static String generateSign(
            final BasePayResponse response, final String mchKey) {
        final SortedMap<String, String> params = signParamsFrom(response);
        final SortedMap<String, String> otherParams = response.getOtherParams();
        if (null != otherParams && !otherParams.isEmpty()) {
            params.putAll(otherParams);
        }
        return generateSign(params, mchKey);
    }

    /**
     * 微信支付-生成签名.
     *
     * @param params 要签名的数据对象.
     * @param mchKey mchKey
     *
     * @return 返回签名 String
     */

    public static String generateSign(
            final SortedMap<String, String> params, final String mchKey) {

        final StringBuilder sb = new StringBuilder();

        for (final Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
        }
        sb.append("key").append('=').append(mchKey);
        return DigestUtils.md5Hex(sb.toString()).toUpperCase(Locale.ENGLISH);
    }

    /**
     * 解密，如：解密【退款结果通知】中的加密信息 req_info 字段.
     *
     * <p>解密步骤如下：
     * <ul>
     * <li>（1）对加密串A做base64解码，得到加密串B</li>
     * <li>（2）对商户mckKey做md5，得到32位小写key</li>
     * <li>（3）用key对加密串B做AES-256-ECB解密（PKCS7Padding）</li>
     * </ul>
     *
     * @param str 待解密的密文
     *
     * @return 解密后的明文
     */
    public static String decrypt(final String str, final String mchKey) {
        final byte[] decode = Base64.getDecoder().decode(str);
        final String keyStr = DigestUtils.md5Hex(mchKey);

        try {
            final Key key = new SecretKeySpec(keyStr.getBytes(), "AES");
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            final byte[] bytes = cipher.doFinal(decode);
            return new String(bytes, "UTF-8");
        } catch (final NoSuchAlgorithmException | InvalidKeyException
                | BadPaddingException | NoSuchPaddingException
                | IllegalBlockSizeException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gzip解压数据.
     *
     * @param data the data
     *
     * @return 解压后的数据
     */
    public static byte[] unCompress(final byte[] data) {
        if (data == null || data.length == 0) {
            return data;
        }
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream();
             final ByteArrayInputStream in = new ByteArrayInputStream(data);
             final GZIPInputStream gzip = new GZIPInputStream(in)) {

            final byte[] buffer = new byte[256];
            int n;
            while ((n = gzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String marshal(final Object obj) {
        try {
            final JAXBContext context = JAXBContext.newInstance(obj.getClass());
            final Marshaller marshaller = context.createMarshaller();
            final StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * jaxb xml 转 pojo.
     *
     * @param <T> the pojo type
     * @param xmlStr the xml str
     * @param clazz the clazz
     *
     * @return the pojo
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(final String xmlStr, final Class<T> clazz) {
        try {
            final JAXBContext context = JAXBContext.newInstance(clazz);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final StringReader reader = new StringReader(xmlStr);
            return (T) unmarshaller.unmarshal(reader);
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }
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
        if (!response.getSign().equals(WeChatPayUtils.generateSign(response, mchKey))) {
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

        return response.getSign().equals(WeChatPayUtils.generateSign(response, mchKey))
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
     * WeChatPayUtils.beansMapFrom(this.otherParams, mapping, Coupon::new);
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

        return WeChatPayUtils.beansFrom(params, mappingMap, Coupon::new);
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
        final Field[] declaredFields = clazz.getDeclaredFields();
        for (final Field field : declaredFields) {
            if (field.getAnnotation(annotationCls) != null) {
                field.setAccessible(true);
                annotatedFields.add(field);
            }
        }

        return annotatedFields;
    }

    private static SortedMap<String, String> signParamsFrom(final Object obj) {
        final Class<?> clazz = obj.getClass();
        final List<Field> fields = CACHE_FOR_SIGN.computeIfAbsent(clazz, clazz0 ->
                getFieldsListWithAnnotation(clazz0, XmlElement.class));
        checkNotEmpty(fields, "fields");

        final SortedMap<String, String> params = new TreeMap<>();
        try {
            for (final Field field : fields) {
                if (null != field.getAnnotation(SignIgnore.class)) {
                    continue;
                }
                final String name = field.getAnnotation(XmlElement.class).name();
                final Object value = field.get(obj);
                final String valueStr = value == null ? null : value.toString();
                if (name.isEmpty()) {
                    continue;
                }
                if (null == valueStr || valueStr.isEmpty()) {
                    continue;
                }
                params.put(name, valueStr);
            }
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return params;
    }
}
