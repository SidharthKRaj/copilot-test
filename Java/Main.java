import java.util.List;

/**
 * Main
 */
/**
 * This class contains the main method that processes data from Utils class.
 * It filters the data to find specific services and codes, and calculates payments.
 * The processed data is then stored in the Utils class.
 */
/**
 * The Main class contains the main method that processes a list of Rsvc objects.
 */
public class Main {

    /**
     * The main method that processes a list of Rsvc objects.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        // Get the data from Utils class
        List<Rsvc> data = Utils.getData();

        // Find the first Rsvc object that contains "LT" in its CPT field
        Rsvc ltSvc = findRsvcByCpt(data, "LT");

        // Find the RCode object with code "IT25" and amount equal to the billed amount of ltSvc
        RCode ltDenial = findRCodeByCodeAndAmount(ltSvc, "IT25", ltSvc != null ? ltSvc.getBilled() : 0);

        // Split the CPT field of ltSvc into an array of strings
        String[] ltCptMod = ltSvc != null ? ltSvc.getCpt().split(",") : new String[0];
        String ltCpt = getCpt(ltCptMod, 0);
        String ltMod = getCpt(ltCptMod, 1);

        // Find the first Rsvc object that contains "RT" in its CPT field
        Rsvc rtSvc = findRsvcByCpt(data, "RT");

        // Find the RCode object with code "IT25" and amount equal to the billed amount of rtSvc
        RCode rtDenial = findRCodeByCodeAndAmount(rtSvc, "IT25", rtSvc != null ? rtSvc.getBilled() : 0);

        // Split the CPT field of rtSvc into an array of strings
        String[] rtCptMod = rtSvc != null ? rtSvc.getCpt().split(",") : new String[0];
        String rtCpt = getCpt(rtCptMod, 0);
        String rtMod = getCpt(rtCptMod, 1);

        // Initialize variables for payment and CPT/modifiers
        double ltPay = 0;
        double rtPay = 0;
        Rsvc svc50 = null;
        String cpt50 = "";
        String mod50 = "";

        // If both ltSvc and rtSvc have the same CPT and have RCode objects with code "IT25",
        // find the first Rsvc object that contains the CPT and "50" in its CPT field
        if (ltCpt.equals(rtCpt) && ltDenial != null && rtDenial != null) {
            svc50 = findRsvcByCptAndModifier(data, rtCpt, "50");

            // If svc50 is not null and has "50" in its modifier field, calculate the payment for ltSvc and rtSvc
            if (svc50 != null) {
                String[] svc50CptMod = svc50.getCpt().split(",");
                cpt50 = getCpt(svc50CptMod, 0);
                mod50 = getCpt(svc50CptMod, 1);

                if (mod50.equals("50")) {
                    ltPay = svc50.getPayment() / 2;
                    rtPay = Utils.sumCurrency(svc50.getPayment(), -ltPay);

                    // For each RCode object in svc50, split the amount and add two new RCode objects to the database
                    for (RCode code : svc50.getCodes()) {
                        double rtCodeAmount = code.getAmount() / 2;
                        double ltCodeAmount = Utils.sumCurrency(code.getAmount(), -rtCodeAmount);
                        code.setSvcId(svc50.getId());
                        Utils.addCode(code);
                    }

                    // Add a new RCode object with code "FAKE" and amount 0 to the database
                    RCode fake = new RCode("FAKE", 0, svc50.getId());
                    Utils.addCode(fake);
                }
            }
        }
    }

    /**
     * Find the first Rsvc object that contains the specified CPT code.
     * @param data The list of Rsvc objects to search.
     * @param cpt The CPT code to search for.
     * @return The first Rsvc object that contains the specified CPT code, or null if no such object is found.
     */
    private static Rsvc findRsvcByCpt(List<Rsvc> data, String cpt) {
        return data.stream()
                .filter(svc -> svc.getCpt().contains(cpt))
                .findFirst()
                .orElse(null);
    }

    /**
     * Find the RCode object with the specified code and amount.
     * @param svc The Rsvc object to search for RCode objects.
     * @param code The code to search for.
     * @param amount The amount to search for.
     * @return The RCode object with the specified code and amount, or null if no such object is found.
     */
    private static RCode findRCodeByCodeAndAmount(Rsvc svc, String code, double amount) {
        if (svc != null) {
            return svc.getCodes().stream()
                    .filter(c -> c.getCode().equals(code) && svc.getBilled() == amount)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * Find the first Rsvc object that contains the specified CPT code and modifier.
     * @param data The list of Rsvc objects to search.
     * @param cpt The CPT code to search for.
     * @param mod The modifier to search for.
     * @return The first Rsvc object that contains the specified CPT code and modifier, or null if no such object is found.
     */
    private static Rsvc findRsvcByCptAndModifier(List<Rsvc> data, String cpt, String mod) {
        return data.stream()
                .filter(svc -> svc.getCpt().contains(cpt) && svc.getCpt().contains(mod))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the CPT code or modifier at the specified index in the array.
     * @param cptMod The array of CPT codes and modifiers.
     * @param index The index of the CPT code or modifier to retrieve.
     * @return The CPT code or modifier at the specified index, or an empty string if the index is out of bounds.
     */
    private static String getCpt(String[] cptMod, int index) {
        if (cptMod.length > index) {
            return cptMod[index];
        }
        return "";
    }
}