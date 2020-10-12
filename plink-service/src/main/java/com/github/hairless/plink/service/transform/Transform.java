package com.github.hairless.plink.service.transform;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.Collection;

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
     * pojo集合对象转DTO对象集合
     *
     * @param pojoList pojo对象集合
     * @return DTO对象集合
     */
    Collection<D> transform(Collection<P> pojoList);

    /**
     * DTO对象转pojo对象
     *
     * @param dto DTO对象
     * @return pojo对象
     */
    P inverseTransform(D dto);

    /**
     * DTO对象集合转pojo对象集合
     *
     * @param dtoList DTO对象集合
     * @return pojo对象集合
     */
    Collection<P> inverseTransform(Collection<D> dtoList);

    /**
     * pojo分页数据对象转DTO分页数据对象
     *
     * @param pageInfoPojo pojo分页数据对象
     * @return DTO分页数据对象
     */
    default PageInfo<D> pageInfoTransform(PageInfo<P> pageInfoPojo) {
        Page<D> page = new Page<>(pageInfoPojo.getPageNum(), pageInfoPojo.getPageSize());
        page.setTotal(pageInfoPojo.getTotal());
        page.addAll(this.transform(pageInfoPojo.getList()));
        return new PageInfo<>(page);
    }
}
