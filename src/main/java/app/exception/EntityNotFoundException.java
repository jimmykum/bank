package app.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entity) {
        super("Entity "+entity+ " does not exist.");
    }
}
