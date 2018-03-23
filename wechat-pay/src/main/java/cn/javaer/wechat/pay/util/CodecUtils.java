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

import cn.javaer.wechat.pay.UncheckedException;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

/**
 * 微信支付工具类.
 *
 * @author zhangpeng
 */
public class CodecUtils {

    static {
        // 对加解密中 PKCS7Padding 模式的支持
        Security.addProvider(new BouncyCastleProvider());
    }

    private CodecUtils() { }

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
            final Key key = new SecretKeySpec(keyStr.getBytes(StandardCharsets.UTF_8), "AES");
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            final byte[] bytes = cipher.doFinal(decode);
            return new String(bytes, "UTF-8");
        }
        catch (final NoSuchAlgorithmException | InvalidKeyException
                | BadPaddingException | NoSuchPaddingException
                | IllegalBlockSizeException | UnsupportedEncodingException e) {
            throw new UncheckedException("Decrypt fail", e);
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
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * jaxb Object to xml String.
     *
     * @param obj Object
     *
     * @return xml String
     */
    public static String marshal(final Object obj) {
        try {
            final JAXBContext context = JAXBContext.newInstance(obj.getClass());
            final Marshaller marshaller = context.createMarshaller();
            final StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            return writer.toString();
        }
        catch (final JAXBException e) {
            throw new UncheckedException(e);
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
        }
        catch (final JAXBException e) {
            throw new UncheckedException(e);
        }
    }

    /**
     * 判断数据是否为 GZIP 压缩数据, 【下载对账单】微信不会对异常信息进行 GZIP, 但对正常的结果会进行 GZIP（如果请求中声明了）.
     *
     * @param bytes data
     *
     * @return isCompressed
     */
    public static boolean isCompressed(final byte[] bytes) {
        if ((bytes == null) || (bytes.length < 2)) {
            return false;
        } else {
            return ((bytes[0] == (byte) (GZIPInputStream.GZIP_MAGIC))
                    && (bytes[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8)));
        }
    }

}
