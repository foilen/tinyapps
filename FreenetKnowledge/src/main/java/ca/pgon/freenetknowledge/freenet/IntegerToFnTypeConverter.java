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
