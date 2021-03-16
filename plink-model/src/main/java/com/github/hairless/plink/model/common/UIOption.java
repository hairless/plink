package com.github.hairless.plink.model.common;

import com.github.hairless.plink.model.enums.UIType;
import lombok.Data;

import java.util.List;

/**
 * @author: silence
 * @date: 2021/1/12
 */
@Data
public class UIOption {

    private String key;
    private String name;
    private UIType uiType;
    private String defaultValue;
    private List<OptionEntry> options;
    private String desc;
    private Boolean enable = true;
    private Boolean hidden = false;

    @Data
    public static class OptionEntry {
        private String label;
        private String value;
        private Boolean enable = true;
    }

}
