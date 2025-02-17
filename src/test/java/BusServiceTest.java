import ENUMS.*;
import model.Company;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import ENUMS.ActivityStatus;
import ENUMS.BusType;
import ENUMS.ComfortRating;

import org.mockito.MockedStatic;
import repository.BusRepository;
import service.BusService;

public class BusServiceTest {


    @Test
    public void testCreateBusSuccess() {
        String busId = "1";
        String busModel = "Volvo";
        String vin = "VIN123";
        int passengerCapacity = 50;
        BusType busType = BusType.COACH;
        ActivityStatus activityStatus = ActivityStatus.ACTIVE;
        ComfortRating comfortRating = ComfortRating.THREE_STARS;
        Company company = new Company("CompanyA", AreaCode.PEJA, "Sample Company");

        try (MockedStatic<BusRepository> mockedBusRepository = mockStatic(BusRepository.class)) {
            mockedBusRepository.when(() ->
                    BusRepository.createBus(busId, busModel, vin, passengerCapacity,
                            busType, activityStatus, comfortRating, company)
            ).thenReturn(true);
            boolean result = BusService.createBus(busId, busModel, vin, passengerCapacity,
                    busType, activityStatus, comfortRating, company);
            assertTrue(result);
        }
    }

    @Test
    public void testCreateBusNullModel() {
        String busId = "3";
        String busModel = " ";
        String vin = "123";
        int passengerCapacity = 50;
        BusType busType = BusType.COACH;
        ActivityStatus activityStatus = ActivityStatus.ACTIVE;
        ComfortRating comfortRating = ComfortRating.THREE_STARS;
        Company company = new Company("CompanyA", AreaCode.PEJA, "Sample Company");

        try (MockedStatic<BusRepository> mockedBusRepository = mockStatic(BusRepository.class)) {
            mockedBusRepository.when(() ->
                    BusRepository.createBus(busId, busModel, vin, passengerCapacity,
                            busType, activityStatus, comfortRating, company)
            ).thenReturn(false);

            boolean result = BusService.createBus(busId, busModel, vin, passengerCapacity,
                    busType, activityStatus, comfortRating, company);

            assertFalse(result);
        }
    }

    @Test
    public void testCreateBusException(){
            String busId = "7";
            String busModel = "Volvo";
            String vin = "VIN999";
            int passengerCapacity = 50;
            BusType busType = BusType.COACH;
            ActivityStatus activityStatus = ActivityStatus.ACTIVE;
            ComfortRating comfortRating = ComfortRating.THREE_STARS;
            Company company = new Company("CompanyA", AreaCode.PEJA, "Sample Company");

            try (MockedStatic<BusRepository> mockedBusRepository = mockStatic(BusRepository.class)) {
                mockedBusRepository.when(() ->
                        BusRepository.createBus(busId, busModel, vin, passengerCapacity,
                                busType, activityStatus, comfortRating, company)
                ).thenThrow(new RuntimeException("Database error"));

                assertThrows(RuntimeException.class, () -> BusService.createBus(busId, busModel, vin, passengerCapacity,
                        busType, activityStatus, comfortRating, company));
            }
        }
    }

