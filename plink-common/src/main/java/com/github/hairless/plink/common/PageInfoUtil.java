package com.github.hairless.plink.common;

import com.github.hairless.plink.model.common.Transform;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * @author: silence
 * @date: 2020/1/17
 */
public class PageInfoUtil {
    public static <T extends Transform<T, F>, F> PageInfo<T> pageInfoTransform(PageInfo<F> pageInfoPO, Class<T> tClass) throws IllegalAccessException, InstantiationException {
        Page<T> page = new Page<>(pageInfoPO.getPageNum(), pageInfoPO.getPageSize());
        page.setTotal(pageInfoPO.getTotal());
        for (F from : pageInfoPO.getList()) {
            T to = tClass.newInstance();
            page.add(to.transform(from));
        }
        return new PageInfo<>(page);
    }
}