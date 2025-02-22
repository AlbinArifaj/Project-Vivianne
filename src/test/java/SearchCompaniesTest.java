import ENUMS.AreaCode;
import databaseConnection.DatabaseUtil;
import model.Company;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import repository.CompanyRepository;
import service.CompanyService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static ENUMS.ActivityStatus.ACTIVE;
import static ENUMS.AreaCode.PRISTINA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SearchCompaniesTest {
@Test
    public void testSearchCompaniesSuccess() throws SQLException {
    List<Company> expectedCompanies = Arrays.asList(
            new Company("1","TestCo", "Desc", PRISTINA, ACTIVE)
    );

    try(MockedStatic<CompanyRepository> mockedRepo= mockStatic(CompanyRepository.class)) {
       mockedRepo.when(() -> CompanyRepository.searchCompanies("TestCo"))
               .thenReturn(expectedCompanies);

       List<Company> companies = CompanyService.searchCompanies("TestCo");

        assertEquals(1, companies.size());
        Company company = companies.get(0);
        assertEquals("TestCo", company.getCompanyName());
        assertEquals(PRISTINA, company.getAreaCode());
        assertEquals("Desc", company.getDescription());
        assertEquals(ACTIVE, company.getCompanyStatus());

        mockedRepo.verify(() -> CompanyService.searchCompanies("TestCo"));

        }
    }

    @Test
    public void testSearchCompaniesNoResultFound() throws  SQLException{
        try(MockedStatic<CompanyRepository> mockedRepo = mockStatic(CompanyRepository.class)) {
            mockedRepo.when(() -> CompanyRepository.searchCompanies("Unknown")).thenThrow(
                    new RuntimeException("No companies found with this name")
            );

            Exception exception = assertThrows(RuntimeException.class, () ->
                CompanyService.searchCompanies("Unknown"));
                assertEquals("No companies found with this name", exception.getMessage());

            mockedRepo.verify(() -> CompanyService.searchCompanies("Unknown"));
        }
    }

    @Test
    public void testSearchCompaniesDatabaseError() {
        try (MockedStatic<CompanyRepository> mockedRepo = mockStatic(CompanyRepository.class)) {
            SQLException sqlEx = new SQLException("Connection failed");

            mockedRepo.when(() -> CompanyRepository.searchCompanies("ErrorTest"))
                    .thenThrow(new RuntimeException("Failed to find companies: " + sqlEx.getMessage()));

            Exception exception = assertThrows(RuntimeException.class, () ->
                    CompanyService.searchCompanies("ErrorTest"));
            assertEquals("Failed to find companies: " + sqlEx.getMessage(), exception.getMessage());

            mockedRepo.verify(() -> CompanyRepository.searchCompanies("ErrorTest"));
        }
    }

    @Test
    public void testSearchCompaniesEmptyName() throws SQLException {
        List<Company> excpectedCompanies = Arrays.asList(
                new Company("1", "TestCo", "Desc" , PRISTINA, ACTIVE)
        );

        try(MockedStatic<CompanyRepository> mockedRepo = mockStatic(CompanyRepository.class)) {
            mockedRepo.when(() -> CompanyRepository.searchCompanies(""))
                            .thenReturn(excpectedCompanies);

            List<Company> companies = CompanyService.searchCompanies("");
            assertFalse(companies.isEmpty());

            mockedRepo.verify(() -> CompanyService.searchCompanies(""));
        }
    }


}
