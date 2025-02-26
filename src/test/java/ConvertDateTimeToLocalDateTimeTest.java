import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import service.DateConversion;
import service.GenerateEmail;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class ConvertDateTimeToLocalDateTimeTest {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Test
    void validLocalDateTimeConversion(){
        LocalDate localDate = LocalDate.now();
        String hour ="04";
        String minutes="23";
        LocalDateTime localDateTime =LocalDateTime.parse(localDate+" "+hour+":"+minutes+":00",dateTimeFormatter);
        assertEquals(localDateTime,DateConversion.fromDateTimeComponents(localDate,hour,minutes));
    }
    @Test
    void invalidLocalDateTimeConversion(){
        LocalDate localDate = LocalDate.now();
        String hour ="4";
        assertNull(DateConversion.fromDateTimeComponents(localDate, hour, null));
    }
    @Test
    void invalidHour() {
        LocalDate date = LocalDate.of(2025, 2, 17);
        assertThrows(DateTimeParseException.class, () -> {
            DateConversion.fromDateTimeComponents(date, "29", "23");
        });
    }
}