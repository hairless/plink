package com.github.hairless.plink.service.transform;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * 类型转换接口
 *
 * @author: silence
 * @date: 2020/1/17
 */
public interface Transform<D, P> {
    /**
     * pojo对象转DTO对象
     *
     * @param pojo pojo对象
     * @return DTO对象
     */
    D transform(P pojo);

    /**
     * DTO对象转pojo对象
     *
     * @param dto DTO对象
     * @return pojo对象
     */
    P inverseTransform(D dto);

    /**
     * pojo分页数据对象转DTO分页数据对象
     *
     * @param pageInfoPojo pojo分页数据对象
     * @return DTO分页数据对象
     */
    default PageInfo<D> pageInfoTransform(PageInfo<P> pageInfoPojo) {
        Page<D> page = new Page<>(pageInfoPojo.getPageNum(), pageInfoPojo.getPageSize());
        page.setTotal(pageInfoPojo.getTotal());
        for (P pojo : pageInfoPojo.getList()) {
            page.add(this.transform(pojo));
        }
        return new PageInfo<>(page);
    }
}
