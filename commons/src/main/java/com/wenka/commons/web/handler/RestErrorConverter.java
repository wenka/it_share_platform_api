package com.wenka.commons.web.handler;

import org.springframework.core.convert.converter.Converter;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
public interface RestErrorConverter<T> extends Converter<RestError, T> {
    T convert(RestError var1);
}
