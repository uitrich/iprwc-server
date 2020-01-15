package nl.iprwc.Utils;

public class Validator {
    private static Validator instance;

    public static synchronized Validator getInstance() {
        if (instance == null) {
            instance = new Validator();
        }
        return instance;
    }

    public enum PasswordStatus {
        TOO_LONG,
        TOO_SHORT,
        INVALID,
        EMPTY,
        NULL, OK
    }

    public enum MailStatus{
        TOO_LONG,
        INVALID,
        EMPTY,
        NULL,
        DUPLICATE,
        NO_SCHOOL_MATCH,
        OK
    }

    public enum NameStatus{
        TOO_LONG,
        INVALID,
        TOO_SHORT,
        EMPTY,
        NULL,
        OK
    }

    public enum ReferenceStatus{
        TOO_LONG,
        INVALID,
        TOO_SHORT,
        EMPTY,
        NULL,
        OK
    }

    public enum PhoneNumberStatus{
        TOO_LONG,
        TOO_SHORT,
        INVALID,
        EMPTY,
        NULL,
        OK
    }

    public enum WebsiteStatus{
        TOO_LONG,
        TOO_SHORT,
        INVALID,
        EMPTY,
        NULL,
        OK
    }

    public static WebsiteStatus isWebsite(String website){
        if(website == null){
            return WebsiteStatus.NULL;
        }
        if(website.length() == 0){
            return WebsiteStatus.EMPTY;
        }
        if(website.length() > 100){
            return WebsiteStatus.TOO_LONG;
        }
        if(website.length() <= 4){
            return WebsiteStatus.TOO_SHORT;
        }
        if(!website.matches("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$")){
            return WebsiteStatus.INVALID;
        }
        return WebsiteStatus.OK;
    }

    public static PhoneNumberStatus isPhoneNumber(String phoneNumber){
        if(phoneNumber == null){
            return PhoneNumberStatus.NULL;
        }
        if(phoneNumber.length() == 0){
            return PhoneNumberStatus.EMPTY;
        }
        if(phoneNumber.length() > 17){
            return PhoneNumberStatus.TOO_LONG;
        }
        if(phoneNumber.length() < 10){
            return PhoneNumberStatus.TOO_SHORT;
        }
        if(!phoneNumber.matches("^((\\+|00(\\s|\\s?\\-\\s?)?)31(\\s|\\s?\\-\\s?)?(\\(0\\)[\\-\\s]?)?|0)[1-9]((\\s|\\s?\\-\\s?)?[0-9])((\\s|\\s?-\\s?)?[0-9])((\\s|\\s?-\\s?)?[0-9])\\s?[0-9]\\s?[0-9]\\s?[0-9]\\s?[0-9]\\s?[0-9]$")){
            return PhoneNumberStatus.INVALID;
        }
        return PhoneNumberStatus.OK;
    }



    public static ReferenceStatus isReference(String reference){
        if(reference == null){
            return ReferenceStatus.NULL;
        }
        if(reference.length() == 0){
            return ReferenceStatus.EMPTY;
        }
        if(reference.length() > 50){
            return ReferenceStatus.TOO_LONG;
        }
        if(reference.length() <= 1){
            return ReferenceStatus.TOO_SHORT;
        }
        /* TODO implement */
        if(false){
            return ReferenceStatus.INVALID;
        }
        return ReferenceStatus.OK;
    }

    public static MailStatus isMailAddress(String mail){
        if(mail == null){
            return MailStatus.NULL;
        }
        if(mail.length() == 0){
            return MailStatus.EMPTY;
        }
        if(!mail.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            return MailStatus.INVALID;
        }
        if(mail.length() > 320){
            return MailStatus.TOO_LONG;
        }
        return MailStatus.OK;
    }

    public static NameStatus isName(String name){
        return isName(name, 50);
    }

    public static NameStatus isName(String name, int maxSize){
        if(name == null){
            return NameStatus.NULL;
        }
        if(name.length() == 0){
            return NameStatus.EMPTY;
        }
        if(name.length() > maxSize){
            return NameStatus.TOO_LONG;
        }
        if(name.length() <= 1){
            return NameStatus.TOO_SHORT;
        }
        if(!name.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")){
            return NameStatus.INVALID;
        }
        return NameStatus.OK;
    }

    public static PasswordStatus isPassword(String password){
        if(password == null){
            return PasswordStatus.NULL;
        }
        if(password.length() == 0){
            return PasswordStatus.EMPTY;
        }
        if(password.length() >= 50){
            return PasswordStatus.TOO_LONG;
        }
        if(password.length() < 8){
            return PasswordStatus.TOO_SHORT;
        }
        if(!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")){
            return PasswordStatus.INVALID;
        }
        return PasswordStatus.OK;
    }
}
