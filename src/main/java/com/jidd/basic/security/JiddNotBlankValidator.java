package com.jidd.basic.security;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.jidd.basic.utils.JiddStringUtils;

/**
 * JiddNotBlankValidator.java
 *
 */
public class JiddNotBlankValidator implements
        ConstraintValidator<JiddNotBlank, String> {
    private String code;
    private String message;


    @Override
    public void initialize(JiddNotBlank constraintAnnotation) {
        code = constraintAnnotation.code();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String obj, ConstraintValidatorContext context) {
        if (JiddStringUtils.isBlank(obj)) {
            String messageTemplate = context.getDefaultConstraintMessageTemplate();
            messageTemplate = JiddStringUtils.isNotBlank(message) ? message : messageTemplate;
            context.disableDefaultConstraintViolation(); //禁用默认的message的值
            context.buildConstraintViolationWithTemplate(messageTemplate)
                    .addNode(code)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}