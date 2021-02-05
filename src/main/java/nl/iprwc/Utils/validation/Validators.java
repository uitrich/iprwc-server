package nl.iprwc.Utils.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Validators {
    private final static Map<String, Validator> validators;

    private Validators() {
        //no instance
    }

    public static void register(String key, Validator validator) {
        if (validators.containsKey(key))
            throw new RuntimeException(
                    "There is already a validator registered with this key, " +
                    "use another key or first unregister the old one.");

        validators.put(key, validator);
    }

    public static void unregister(String key) {
        validators.remove(key);
    }


    public static Validator fromKey(String key) {
        if (!validators.containsKey(key))
            throw new UnsupportedOperationException("The key does not point to a existing validator");

        return validators.get(key);
    }

    public static ValidatorResult validate(String key, String input) {
        return validate(fromKey(key), input);
    }

    public static ValidatorResult validate(Validator validator, String input) {
        return validator.test(input);
    }

    public static ValidatorResult isUsername(String input)    { return validate("username", input); }
    public static ValidatorResult isPassword(String input)    { return validate("password", input); }
    public static ValidatorResult isMailAddress(String input) { return validate("mailAddress", input); }
    public static ValidatorResult isPhoneNumber(String input) { return validate("phoneNumber", input); }
    public static ValidatorResult isTag(String input)         { return validate("tag", input); }
    public static ValidatorResult isCategory(String input)    { return validate("category", input); }
    public static ValidatorResult isWebsite(String input)     { return validate("website", input); }
    public static ValidatorResult isReference(String input)   { return validate("reference", input); }
    public static ValidatorResult isName(String input)        { return validate("name", input); }

    static {
        validators = new HashMap<>();

        register("password", Validator
                .builder()
                .minimumLength(8)
                .maximumLength(60)
                .typeable()
                .find("[a-z]+")
                .find("[A-Z]+")
                .find("[\\d]+")
                .find("[\\W_]+")
                .build());

        register("mailAddress", Validator
                .builder()
                .maximumLength(256) // RFC 5321
                .matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\""
                       + "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\"
                       + "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+"
                       + "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])"
                       + "|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])"
                       + "|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]"
                       + "|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
                        Pattern.CASE_INSENSITIVE)
                .build());

        register("category", Validator
                .builder()
                .minimumLength(3)
                .maximumLength(50)
                .matches("[a-zA-Z0-9][\\w\\s-]*[a-zA-Z0-9]")
                .build());

        register("website", Validator
                .builder()
                .notEmpty()
                .maximumLength(2048)
                .matches("(https?://)([a-zA-Z0-9]+(-?[a-zA-Z0-9])*\\.)+[\\w]{2,}(/\\S*)?")
                .build());

        register("reference", Validator
                .builder()
                .notEmpty()
                .maximumLength(50)
                .matches("[\\w-]+")
                .build());

        register("name", Validator
                .builder()
                .minimumLength(2)
                .maximumLength(80)
                .typeable()
                .build());
    }
}
