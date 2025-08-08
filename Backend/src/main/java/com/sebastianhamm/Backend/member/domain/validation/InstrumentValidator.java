/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.member.domain.validation;


import com.sebastianhamm.Backend.member.domain.enums.InstrumentEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstrumentValidator implements ConstraintValidator<InstrumentValidation, InstrumentEnum> {

    @Override
    public boolean isValid(InstrumentEnum value, ConstraintValidatorContext context) {
        return value != null;
    }
}
