package at.sebastianhamm.backend.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String ex){
        super(ex);
    }
}