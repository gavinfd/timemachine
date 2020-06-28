package com.spacetime.tardis.validations;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ValidationRegex {
    public static final String PERSONAL_GALACTIC_IDENTIFIER = "^([a-zA-Z]{1}[a-zA-Z0-9]{4,9})$";
    public static final String PGI_LENGTH_MESSAGE = "Personal galactic identifier must be between 5 and 10 characters in length.";
    public static final String PGI_REGEX_MESSAGE = "Personal galactic identifier must start with a letter and only contain alphanumeric characters.";
}
