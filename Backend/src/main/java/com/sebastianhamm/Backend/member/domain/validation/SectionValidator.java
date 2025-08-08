/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.member.domain.validation;


import com.sebastianhamm.Backend.member.domain.enums.SectionEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SectionValidator implements ConstraintValidator<SectionValidation, SectionEnum> {
    @Override
    public boolean isValid(SectionEnum value, ConstraintValidatorContext context) {
        return value != null;
    }
}
