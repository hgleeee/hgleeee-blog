package com.hgleeee.blog.oauth2.converter;

public interface UserInfoConverter<T, R> {
    R convert(T t);

}
