import ENUMS.*;
import model.Bus;
import model.Company;
import model.filter.BusLineFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import ENUMS.ActivityStatus;
import ENUMS.BusType;
import ENUMS.ComfortRating;

import org.mockito.MockedStatic;
import repository.BusRepository;
import service.BusService;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public void testCreateBusException() {
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

    @Test
    public void testGetBusList() {
        model.filter.Bus filter = new model.filter.Bus("VIN123");

        model.Bus bus1 = new model.Bus("bus1", "Volvo", "VIN123", 50, BusType.COACH, ActivityStatus.ACTIVE, ComfortRating.THREE_STARS);
        model.Bus bus2 = new model.Bus("bus2", "Mercedes", "VIN1234", 40, BusType.MINIBUS, ActivityStatus.ACTIVE, ComfortRating.FOUR_STARS);
        List<model.Bus> expectedList = Arrays.asList(bus1, bus2);


        try (MockedStatic<BusRepository> mockedRepo = mockStatic(BusRepository.class)) {
            mockedRepo.when(() -> BusRepository.getByFilter(filter)).thenReturn(expectedList);
            List<model.Bus> actualList = BusService.getBusList(filter);

            assertEquals(expectedList, actualList);
        }
    }

    @Test
    public void testGetBusListEdge() {
        model.filter.Bus filter = new model.filter.Bus("NonExistentVin");

        List<Bus> expectedList = Collections.emptyList();

        try (MockedStatic<BusRepository> mockedRepo = mockStatic(BusRepository.class)) {
            mockedRepo.when(() -> BusRepository.getByFilter(filter)).thenReturn(expectedList);
            List<Bus> actualList = BusService.getBusList(filter);

            assertEquals(expectedList, actualList);
        }
    }

    @Test
    public void testGetBusListException() {
        model.filter.Bus filter = new model.filter.Bus("VIN_ERROR");

        try (MockedStatic<BusRepository> mockedRepo = mockStatic(BusRepository.class)) {
            mockedRepo.when(() -> BusRepository.getByFilter(filter))
                    .thenThrow(new RuntimeException("Repository error"));

            assertThrows(RuntimeException.class, () -> {
                BusService.getBusList(filter);
            });
        }
    }
}