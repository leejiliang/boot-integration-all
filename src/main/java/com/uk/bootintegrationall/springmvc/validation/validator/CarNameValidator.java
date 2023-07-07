package com.uk.bootintegrationall.springmvc.validation.validator;

import com.uk.bootintegrationall.springmvc.validation.CarNameLegal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Description TODO
 */
public class CarNameValidator implements ConstraintValidator<CarNameLegal, String> {
    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !value.contains("dog");
    }

    /**
     * Initializes the validator in preparation for
     * {@link CarNameValidator#isValid(String, ConstraintValidatorContext)} calls.
     * The constraint annotation for a given constraint declaration
     * is passed.
     * <p>
     * This method is guaranteed to be called before any use of this instance for
     * validation.
     * <p>
     * The default implementation is a no-op.
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(CarNameLegal constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        System.out.println("CarNameValidator.initialize");
    }
}
