package com.github.hairless.plink.model.common;

import lombok.Getter;

import java.util.List;
import java.util.function.Function;

/**
 * @author: silence
 * @date: 2021/1/12
 */
@Getter
public class UIOption {
    public static UIOption SELECT = new UIOption("select");
    public static UIOption RADIO = new UIOption("radio");
    public static UIOption INPUT = new UIOption("input");

    private final String uiType;
    private String defaultValue;
    private List<String> options;
    private String desc;
    private Function<String, Boolean> validator;

    private UIOption(String uiType) {
        this.uiType = uiType;
    }

    public UIOption defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }


    public UIOption options(List<String> options) {
        this.options = options;
        return this;
    }

    public UIOption desc(String desc) {
        this.desc = desc;
        return this;
    }

    public UIOption validator(Function<String, Boolean> validator) {
        this.validator = validator;
        return this;
    }

}
