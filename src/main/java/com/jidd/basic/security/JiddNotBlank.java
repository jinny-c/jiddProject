package com.jidd.basic.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.jidd.project.common.JiddProjectConstants;

/**
 * 非空校验
 * 
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JiddNotBlankValidator.class)
public @interface JiddNotBlank {
	String code() default JiddProjectConstants.E_NULL_ERROR;

	String message() default "field invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}