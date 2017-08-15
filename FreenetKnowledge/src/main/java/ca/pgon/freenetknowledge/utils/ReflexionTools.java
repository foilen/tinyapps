/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2017

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package ca.pgon.freenetknowledge.utils;

import java.lang.reflect.Method;
import java.util.Locale;

public class ReflexionTools {

    /**
     * Copy the properties from one bean to another. The type must be exactly the same.
     *
     * @param src
     *            the source bean
     * @param dst
     *            the destination bean
     * @param names
     *            the names of the properties to copy
     */
    static public void copyGetToSet(Object src, Object dst, String... names) {
        try {
            for (String name : names) {
                // Get the function names
                name = String.valueOf(name.charAt(0)).toUpperCase(Locale.ENGLISH) + name.substring(1);
                String getName = "get" + name;
                String setName = "set" + name;

                // Get value and type
                Method getMethod = src.getClass().getMethod(getName, (Class<?>[]) null);
                Object value = getMethod.invoke(src);
                Class<?> type = getMethod.getReturnType();

                // Set value
                Method setMethod = src.getClass().getMethod(setName, type);
                setMethod.invoke(dst, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Copy the properties from one bean to another. The type must be exactly the same.
     *
     * @param src
     *            the source bean
     * @param dst
     *            the destination bean
     * @param names
     *            the names of the properties to copy
     */
    static public void copyIsToSet(Object src, Object dst, String... names) {
        try {
            for (String name : names) {
                // Get the function names
                name = String.valueOf(name.charAt(0)).toUpperCase(Locale.ENGLISH) + name.substring(1);
                String getName = "is" + name;
                String setName = "set" + name;

                // Get value and type
                Method getMethod = src.getClass().getMethod(getName, (Class<?>[]) null);
                Object value = getMethod.invoke(src);
                Class<?> type = getMethod.getReturnType();

                // Set value
                Method setMethod = src.getClass().getMethod(setName, type);
                setMethod.invoke(dst, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
