package exception;

import java.util.function.Supplier;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public static Supplier<EntityNotFoundException> supplier(String massage) {
        return () -> new EntityNotFoundException(massage);
    }
}
