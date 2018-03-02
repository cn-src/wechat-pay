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

package util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author zhangpeng
 */
public class TestUtils {

    @SuppressWarnings("unchecked")
    public static <T> T jaxbUnmarshal(final String xmlStr, final Class<T> clazz) {
        try {
            final JAXBContext context = JAXBContext.newInstance(clazz);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final StringReader reader = new StringReader(xmlStr);
            return (T) unmarshaller.unmarshal(reader);
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static String jaxbMarshal(final Object obj) {
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

    public static String readClassPathFileToUTFString(final String fileClassPath, final Class clazzForClassPath) {
        try {
            final byte[] bytes = Files.readAllBytes(Paths.get(clazzForClassPath.getResource(fileClassPath).getFile()));
            return new String(bytes, "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
