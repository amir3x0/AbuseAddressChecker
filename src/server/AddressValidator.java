package server;

public class AddressValidator {
    // בדוק אם הכתובת בפורמט ביטקוין בסיסי (1... או 3...)
    public boolean isValid(String address) {
        return address.matches("^[13][a-km-zA-HJ-NP-Z1-9]{25,34}$");
    }
}