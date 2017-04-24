package com.wenka.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * Created by 文卡<wkwenka@gmail.com> on 2017/4/2.
 */
public class StringToEnumConverterFactory implements ConverterFactory<String,Enum> {

    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnum(targetType);
    }
    private class StringToEnum<T extends Enum> implements Converter<String, T> {

        private final Class<T> enumType;

        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        public T convert(String source) {
            if (StringUtils.isBlank(source)) {
                return null;
            }
            return (T) Enum.valueOf(this.enumType, source.trim());
        }
    }
}
