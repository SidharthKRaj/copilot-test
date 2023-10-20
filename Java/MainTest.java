import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testFindRsvcByCpt() {
        // Positive test cases
        Rsvc rsvc1 = new Rsvc("CPT123");
        Rsvc rsvc2 = new Rsvc("CPT456");
        Rsvc rsvc3 = new Rsvc("CPT789");
        List<Rsvc> data = Arrays.asList(rsvc1, rsvc2, rsvc3);
        assertEquals(rsvc1, Main.findRsvcByCpt(data, "123"));
        assertEquals(rsvc2, Main.findRsvcByCpt(data, "456"));
        assertEquals(rsvc3, Main.findRsvcByCpt(data, "789"));

        // Negative test cases
        assertNull(Main.findRsvcByCpt(data, "000"));
        assertNull(Main.findRsvcByCpt(data, "999"));
    }
}