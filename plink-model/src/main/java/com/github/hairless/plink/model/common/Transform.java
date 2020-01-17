package com.github.hairless.plink.model.common;

/**
 * 类型转换接口
 *
 * @author: silence
 * @date: 2020/1/17
 */
public interface Transform<T, F> {
    T transform(F from);
}
