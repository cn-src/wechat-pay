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

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.File;
import java.io.IOException;

/**
 * @author zhangpeng
 */
public class AssertJson {
    public static void assertWithJson(final Object obj, final String filePath) {
        try {
            final File file = new File(obj.getClass().getResource("/assert-json/" + filePath).getFile());
            final String objJsonStr = JSON.toJSONString(obj);
            if (file.exists()) {
                final String str = FileUtils.readFileToString(file, "UTF-8");
                JSONAssert.assertEquals(str, objJsonStr, false);
            } else {
                FileUtils.writeStringToFile(file, objJsonStr, "UTF-8");
                throw new AssertionError();
            }
        } catch (final JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
