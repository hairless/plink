package com.github.hairless.plink.model.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

/**
 * Created by silence on 2020/01/10.
 */
@Getter
@Setter
public class BaseModel {
    @Id
    private Long id;

}
