package com.tuum.cbs.helpers.validators;

import java.util.List;

public interface RequestValidatorI {
    void validate();

    boolean isValid();

    List<String> getValidationResults(); // TODO: Discuss the necessity
}
