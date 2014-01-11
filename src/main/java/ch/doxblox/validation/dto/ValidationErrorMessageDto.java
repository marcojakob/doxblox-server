package ch.doxblox.validation.dto;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class that contains a list of {@link ValidationErrorDto} objects.
 * 
 * @author Marco Jakob
 */
public class ValidationErrorMessageDto {

    private List<ValidationErrorDto> errors = new ArrayList<ValidationErrorDto>();

    public ValidationErrorMessageDto() {

    }

    public void addError(String path, String message) {
        ValidationErrorDto fieldError = new ValidationErrorDto(path, message);
        errors.add(fieldError);
    }

    public List<ValidationErrorDto> getErrors() {
        return errors;
    }
}