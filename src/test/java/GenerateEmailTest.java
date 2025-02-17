import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import service.GenerateEmail;

public class GenerateEmailTest {
    @Test
   void validGenerateEmail(){
       assertEquals("albinarifaj1@vivianne.com",GenerateEmail.email("albin","arifaj",1));
   }
    @Test
    void invalidGenerateEmail(){
        assertNotEquals("albinarifaj3@vivianne.com",GenerateEmail.email("albin","arifaj",1));
    }
    @Test
    void edgeCaseGenerateEmail(){
        assertEquals("albinarifaj@vivianne.com",GenerateEmail.email("albin","arifaj",0));
    }
}