package exception;

import java.util.function.Supplier;

public class RegistrationException extends RuntimeException {
    public RegistrationException(String massage) {
        super(massage);
    }

    public static Supplier<RegistrationException> supplier(String massage) {
        return () -> new RegistrationException(massage);
    }
}
