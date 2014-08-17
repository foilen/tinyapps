/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014

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
package ca.pgon.freenetknowledge.freenet;

import org.springframework.core.convert.converter.Converter;

public class IntegerToFnTypeConverter implements Converter<Integer, fnType> {

	@Override
	public fnType convert(Integer integer) {
		for (fnType type : fnType.values()) {
			if (type.ordinal() == integer) {
				return type;
			}
		}
		return null;
	}

}
