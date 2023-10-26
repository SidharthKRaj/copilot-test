import java.util.HashMap;
import java.util.Map;

public class DiagnosisCode {
    private static Map<String, String> diagnosisCodes = new HashMap<String, String>() {{
        put("A00", "Cholera");
        put("A01", "Typhoid and paratyphoid fevers");
        put("A02", "Other salmonella infections");
        // ...
    }};

    public static void validate(String code) throws Exception {
        if (!code.matches("^[A-Z0-9]{3}$")) {
            throw new Exception("Invalid diagnosis code: " + code);
        }
        if (!Character.isLetter(code.charAt(0))) {
            throw new Exception("Invalid diagnosis code: " + code);
        }
        if (!code.substring(1).matches("^[0-9]{2}$")) {
            throw new Exception("Invalid diagnosis code: " + code);
        }
    }

    public static String getDescription(String code) throws Exception {
        String description = diagnosisCodes.get(code);
        if (description == null) {
            throw new Exception("Invalid diagnosis code: " + code);
        }
        return description;
    }
}