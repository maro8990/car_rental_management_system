package java_project;

//etimi klasi stin java gia na mporo na xrisimopoiiso eukolotera tis imerominies
import java.time.LocalDate;

//Interface pou ilopoieitai apo ola ta oximata pou mporoun na enoikiastoun
public interface Bookable {

    // Dilonei ti methodo elegxou diathesimotitas enos oximatos
    // gia sigkekrimeni imerominia enarksis kai arithmo imeron
    boolean isAvailable(LocalDate start, int days);
}