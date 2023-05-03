package com.evolunteer.evm.common.utils;

import com.evolunteer.evm.common.domain.api.ApiError;
import com.evolunteer.evm.common.domain.exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Date: 02.06.22
 *
 * @author ilia
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {

    public static final String ONLY_DIGEST_REGEX = "\\d+";
    public static final String ONLY_NUMBERS_REGEX = "\\d+(\\.\\d+)?";
    public static final String NUMBER_REGEX = "^(380)*([0-9]{9})$";
    public static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?|^((http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!*])(?=\\S+$).{8,}$";

    private static final Validator javaxValidator =
            Validation.buildDefaultValidatorFactory().usingContext().getValidator();
    private static final String FIELD_PATTERN = "{validatedField}";

    public static <T> void validate(final T validationObject, final Class<?>... clazz) {
        if (Objects.isNull(validationObject)) {
            throw new ValidationException("Validation target can not be null");
        }
        final Set<ConstraintViolation<T>> validationResult = javaxValidator.validate(validationObject, clazz);
        handleValidationResult(validationResult);
    }

    public static <T> void validate(final Collection<T> validationCollection) {
        if (Objects.isNull(validationCollection)) {
            throw new ValidationException("Validation target can not be null");
        }
        validationCollection.forEach(ValidationUtils::validate);
    }

    public static boolean match(final String string, final String regex) {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    private static <T> void handleValidationResult(final Set<ConstraintViolation<T>> validationResult) {
        if (Objects.nonNull(validationResult) && !validationResult.isEmpty()) {
            final List<ApiError> errors = validationResult.stream()
                    .map(ValidationUtils::map)
                    .collect(Collectors.toList());
            throw new ValidationException(errors);
        }
    }

    private static <T> ApiError map(final ConstraintViolation<T> elem) {
        final String field = ((PathImpl) elem.getPropertyPath()).getLeafNode().asString();
        return ApiError.of(field, elem.getMessage().replace(FIELD_PATTERN, field));
    }
}
