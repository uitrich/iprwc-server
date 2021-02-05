package nl.iprwc.Utils.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private List<ValidatorFunction> validators;

    public static ValidatorBuilder builder() {
        return new ValidatorBuilder();
    }

    public static Validator copyFrom(String key) {
        return copyFrom(Validators.fromKey(key));
    }

    public static Validator copyFrom(Validator original) {
        Validator clone = new Validator();
        clone.setValidators(original.validators);
        return clone;
    }

    private void setValidators(List<ValidatorFunction> validators) {
        this.validators = validators;
    }

    private Validator() { }

    public ValidatorResult test(String input) {
        if (input == null)
            return ValidatorResult.ok;

        for (ValidatorFunction validator : validators) {
            ValidatorResult result = validator.test(input);
            if (result != ValidatorResult.ok) return result;
        }

        return ValidatorResult.ok;
    }

    public static class ValidatorBuilder {
        private final List<ValidatorFunction> validators = new ArrayList<>();

        public ValidatorBuilder minimumLength(int length) {
            validators.add(input -> input.length() >= length ?ValidatorResult.ok :ValidatorResult.tooShort);
            return this;
        }

        public ValidatorBuilder maximumLength(int length) {
            validators.add(input -> input.length() <= length ?ValidatorResult.ok :ValidatorResult.tooLong);
            return this;
        }

        public ValidatorBuilder length(int length) {
            return minimumLength(length).maximumLength(length);
        }

        public ValidatorBuilder notEmpty() {
            validators.add(input -> input.length() == 0 ?ValidatorResult.empty :ValidatorResult.ok);
            return this;
        }

        public ValidatorBuilder matches(String regex, int flags) {
            validators.add(input -> {
                Pattern p = Pattern.compile(regex, flags);
                Matcher m = p.matcher(input);
                return m.matches() ?ValidatorResult.ok :ValidatorResult.invalid;
            });

            return this;
        }

        public ValidatorBuilder matches(String regex) {
            return matches(regex, 0);
        }

        public ValidatorBuilder find(String regex, int flags) {
            validators.add(input -> {
                Pattern p = Pattern.compile(regex, flags);
                Matcher m = p.matcher(input);
                return m.find() ?ValidatorResult.ok :ValidatorResult.invalid;
            });

            return this;
        }

        public ValidatorBuilder find(String regex) {
            return find(regex, 0);
        }

        public ValidatorBuilder includes(List<String> items, boolean caseSensitive) {
            validators.add(input
                    -> items.stream().anyMatch(s -> caseSensitive ? s.equals(input) : s.equalsIgnoreCase(input))
                    ? ValidatorResult.ok
                    : ValidatorResult.invalid);

            return this;
        }

        public ValidatorBuilder includes(List<String> items) {
            return includes(items, true);
        }

        public ValidatorBuilder excludes(List<String> items, boolean caseSensitive) {
            validators.add(input
                    -> items.stream().noneMatch(s -> caseSensitive ? s.equals(input) : s.equalsIgnoreCase(input))
                    ? ValidatorResult.ok
                    : ValidatorResult.invalid);

            return this;
        }

        public ValidatorBuilder excludes(List<String> items) {
            return excludes(items, true);
        }

        public ValidatorBuilder custom(ValidatorFunction tester) {
            validators.add(tester);
            return this;
        }

        public ValidatorBuilder numeric() {
            return matches("^[\\d]+$");
        }

        public ValidatorBuilder typeable() {
            // List of characters: https://en.wikipedia.org/wiki/List_of_Unicode_characters
            return matches("[\\u0020-\\u007e\\u00a1-\\u0148\\u014a-\\u017f]*");
        }

        public Validator build() {
            Validator validator = new Validator();
            validator.setValidators(validators);
            return validator;
        }
    }
}
