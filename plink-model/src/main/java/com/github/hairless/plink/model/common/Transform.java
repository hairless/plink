package com.github.hairless.plink.model.common;

/**
 * @author: silence
 * @date: 2020/1/17
 *
 * 类型转换接口
 */
public interface Transform<T, F> {
    T transform(F from);
}
