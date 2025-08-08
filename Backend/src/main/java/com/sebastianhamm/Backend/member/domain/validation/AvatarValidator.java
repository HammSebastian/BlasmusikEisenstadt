/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.member.domain.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;
import java.util.List;

public class AvatarValidator implements ConstraintValidator<AvatarValidation, String> {
    private static final List<String> ALLOWED_DOMAINS = List.of("stadtkapelle-eisenstadt.at", "api.stadtkapelle-eisenstadt.at");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            URI uri = new URI(value);
            String host = uri.getHost();
            return host != null && ALLOWED_DOMAINS.stream().anyMatch(host::endsWith);
        } catch (Exception e) {
            return false;
        }
    }
}
