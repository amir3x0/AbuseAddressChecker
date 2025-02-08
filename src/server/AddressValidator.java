package server;

public class AddressValidator {
    // בדוק אם הכתובת בפורמט ביטקוין בסיסי (1... או 3...)
    public boolean isValid(String address) {
    	return address.matches("^(1[a-km-zA-HJ-NP-Z1-9]{25,34}|3[a-km-zA-HJ-NP-Z1-9]{25,34}|bc1[a-zA-HJ-NP-Z0-9]{11,80})$");
    }
}