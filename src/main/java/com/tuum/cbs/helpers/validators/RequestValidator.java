package com.tuum.cbs.helpers.validators;

import java.util.ArrayList;
import java.util.List;

public abstract class RequestValidator implements RequestValidatorI {
    public List<String> validationResults = new ArrayList<>();

    @Override
    public abstract void validate();

    @Override
    public boolean isValid() {
        return validationResults.size() == 0;
    }

    @Override
    public List<String> getValidationResults() {
        return validationResults;
    }
}
