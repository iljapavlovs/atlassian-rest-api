package atlassian.rest.api.exceptions;

public class ConfluenceUpdaterException extends RuntimeException{

    public ConfluenceUpdaterException(String message){
        super(message);
    }

    public ConfluenceUpdaterException(String message, Throwable cause){
        super(message, cause);
    }
}
