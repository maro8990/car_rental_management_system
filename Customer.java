package java_project;

public class Customer {
    private String fullName;
    private String idNumber;
    private String address;
    private String phone;
    private String email;
    private String creditCardNumber;
    private LoyaltyAccount loyaltyAccount;

    public Customer(String fullName, String idNumber, String address, String phone, String email,
                    String creditCardNumber, LoyaltyAccount loyaltyAccount) {
        this.fullName = fullName;
        this.idNumber = idNumber;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.creditCardNumber = creditCardNumber;
        this.loyaltyAccount = loyaltyAccount;
    }

    public String getFullName() {
        return fullName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public LoyaltyAccount getLoyaltyAccount() {
        return loyaltyAccount;
    }

    //Override
    public String toString() {
        return "Customer [fullName=" + fullName + ", idNumber=" + idNumber + ", address=" + address
                + ", phone=" + phone + ", email=" + email + ", creditCardNumber=" + creditCardNumber
                + ", loyaltyAccount=" + loyaltyAccount.getLoyaltyCardNumber()
                + ", points=" + loyaltyAccount.getPoints() + "]";
    }
}