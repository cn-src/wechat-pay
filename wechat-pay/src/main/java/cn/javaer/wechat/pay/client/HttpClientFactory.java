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

package cn.javaer.wechat.pay.client;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * @author zhangpeng
 */
public class HttpClientFactory {
    private final String key;
    private final String certificatePath;

    public HttpClientFactory(final String key, final String certificatePath) {
        this.key = key;
        this.certificatePath = certificatePath;
    }

    public HttpClient build() {

        try {
            // 配置证书
            final KeyStore keystore = KeyStore.getInstance("PKCS12");
            final char[] keyArray = this.key.toCharArray();
            keystore.load(new FileInputStream(this.certificatePath), keyArray);

            // ssl
            final SSLContext sslContext = SSLContexts.custom()
                    .loadKeyMaterial(keystore, keyArray).build();
            final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                    new String[]{"TLSv1"}, null, new DefaultHostnameVerifier());

            // httpclient
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (final IOException | NoSuchAlgorithmException | CertificateException
                | UnrecoverableKeyException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
