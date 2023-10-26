public class StringUtil {
    public String reverseString(String string) {
        return new StringBuilder(string).reverse().toString();
    }

    public String uppercaseString(String string) {
        return string.toUpperCase();
    }

    public String lowercaseString(String string) {
        return string.toLowerCase();
    }

    public String removeWhitespace(String string) {
        return string.replaceAll("\\s+", "");
    }

    public int countWords(String string) {
        return string.trim().split("\\s+").length;
    }

    public int stringToInt(String string) {
        return Integer.parseInt(string);
    }

    public float stringToFloat(String string) {
        return Float.parseFloat(string);
    }

    public String intToString(int number) {
        return Integer.toString(number);
    }

    public String floatToString(float number) {
        return String.format("%.2f", number);
    }
}