import controller.ManageBus;
import javafx.scene.shape.SVGPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import java.lang.reflect.Field;

public class SetBusRatingTest {
    private ManageBus manageBus;

    @BeforeEach
    void setUp() throws Exception {
        manageBus = new ManageBus();
        SVGPath[] svgStars = new SVGPath[5];
        for (int i = 0; i < svgStars.length; i++) {
            svgStars[i] = mock(SVGPath.class);
        }
        setPrivateField(manageBus, "svgStar1", svgStars[0]);
        setPrivateField(manageBus, "svgStar2", svgStars[1]);
        setPrivateField(manageBus, "svgStar3", svgStars[2]);
        setPrivateField(manageBus, "svgStar4", svgStars[3]);
        setPrivateField(manageBus, "svgStar5", svgStars[4]);
    }
    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
    @Test
    void testSetBusRatingSuccessful() throws IllegalAccessException, NoSuchFieldException {
        manageBus.changeColors(3);
        Field cRatingField = ManageBus.class.getDeclaredField("cRating");
        cRatingField.setAccessible(true);
        int cRatingValue = (int) cRatingField.get(manageBus);
        assertEquals(cRatingValue,3);
    }
    @Test
    void testSetBusRatingFailed() throws IllegalAccessException, NoSuchFieldException {
        manageBus.changeColors(3);
        Field cRatingField = ManageBus.class.getDeclaredField("cRating");
        cRatingField.setAccessible(true);
        int cRatingValue = (int) cRatingField.get(manageBus);
        assertNotEquals(cRatingValue,2);
    }
    @Test
    void testSetBusRatingInvalidValue() throws IllegalAccessException, NoSuchFieldException {
        manageBus.changeColors(-1);
        Field cRatingField = ManageBus.class.getDeclaredField("cRating");
        cRatingField.setAccessible(true);
        int cRatingValue = (int) cRatingField.get(manageBus);
        assertEquals(cRatingValue,0);
    }
}