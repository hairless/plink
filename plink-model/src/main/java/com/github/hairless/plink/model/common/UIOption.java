package com.github.hairless.plink.model.common;

import lombok.Getter;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author: silence
 * @date: 2021/1/12
 */
@Getter
public class UIOption {
    public static UIOption SELECT = new UIOption("select");
    public static UIOption RADIO = new UIOption("radio");
    public static UIOption INPUT = new UIOption("input");
    public static UIOption NUMBER = new UIOption("number");

    private final String uiType;
    private String name;
    private String defaultValue;
    private List<String> options;
    private String desc;
    private Consumer<String> validator;

    private UIOption(String uiType) {
        this.uiType = uiType;
    }

    public UIOption name(String name) {
        this.name = name;
        return this;
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

    public UIOption validator(Consumer<String> validator) {
        this.validator = validator;
        return this;
    }

}
