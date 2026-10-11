package java_project;

// Diki mou custom exception gia tin periptosi pou enas pelatis den iparxei sto sistima
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}