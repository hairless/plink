package com.github.hairless.plink.common;

import com.github.hairless.plink.model.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * @author: silence
 * @date: 2020/1/19
 */
public class ValidatorUtil {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static void validate(Object obj) {
        Set<ConstraintViolation<Object>> set = validator.validate(obj, Default.class);
        if (set != null && set.size() > 0) {
            throw new ValidationException(set.stream().findFirst().get().getMessage());
        }
    }
}