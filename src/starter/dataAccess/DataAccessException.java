package dataAccess;

public class DataAccessException extends Exception {

    private static final long serialVersionUID = 1L;
    private String errorMessage = "This is the data access exception";

    public DataAccessException(String errorMessage){
        this.errorMessage = errorMessage;
    }

    //Getter method for errorMessage
    public String getErrorMessage() {
        return errorMessage;
    }
}