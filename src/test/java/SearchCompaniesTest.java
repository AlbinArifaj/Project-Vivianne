import ENUMS.AreaCode;
import databaseConnection.DatabaseUtil;
import model.Company;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import repository.CompanyRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ENUMS.ActivityStatus.ACTIVE;
import static ENUMS.AreaCode.PRISTINA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SearchCompaniesTest {
@Test
    public void testSearchCompaniesSuccess() throws SQLException {
    try(MockedStatic<DatabaseUtil> mockedDatabaseUtil= mockStatic(DatabaseUtil.class)) {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt= mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        mockedDatabaseUtil.when(DatabaseUtil::getConnection).thenReturn(mockConn);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true).thenReturn(false);
        when(mockRs.getString("company_name")).thenReturn("TestCo");
        when(mockRs.getString("area_code")).thenReturn("PRISTINA");
        when(mockRs.getString("description")).thenReturn("Desc");
        when(mockRs.getString("company_status")).thenReturn("ACTIVE");

        List<Company> companies = CompanyRepository.searchCompanies("TestCo");

        assertEquals(1, companies.size());
        Company company = companies.get(0);
        assertEquals("TestCo", company.getCompanyName());
        assertEquals(PRISTINA, company.getAreaCode());
        assertEquals("Desc", company.getDescription());
        assertEquals(ACTIVE, company.getCompanyStatus());

        verify(mockStmt).setString(1, "%TestCo%");
        }
    }

    @Test
    public void testSearchCompaniesNoResultFound() throws  SQLException{
        try(MockedStatic<DatabaseUtil> mockedDatabaseUtil = mockStatic(DatabaseUtil.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);
            ResultSet mockRs = mock(ResultSet.class);

            mockedDatabaseUtil.when(DatabaseUtil::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockRs);

            when(mockRs.next()).thenReturn(false);

            Exception exception = assertThrows(RuntimeException.class, () ->
                CompanyRepository.searchCompany("Unknown"));
                assertEquals("No companies found with this name", exception.getMessage());
        }
    }

    @Test
    public void testSearchCompaniesDatabaseError() throws SQLException{
        try(MockedStatic<DatabaseUtil> mockedDatabaseUtil = mockStatic(DatabaseUtil.class)) {
            Connection mockConn = mock(Connection.class);
            mockedDatabaseUtil.when(DatabaseUtil::getConnection).thenReturn(mockConn);

            SQLException sqlEx = new SQLException("COnnection failed");
            when(mockConn.prepareStatement(anyString())).thenThrow(sqlEx);

            Exception exception = assertThrows(RuntimeException.class, () ->
                    CompanyRepository.searchCompanies("ErrorTest"));
            assertEquals("Failed to find companies: " + sqlEx.getMessage(), exception.getMessage());

        }
    }

    @Test
    public void testSearchCompaniesEmptyName() throws SQLException {
        try(MockedStatic<DatabaseUtil> mockedDatabaseUtil = mockStatic(DatabaseUtil.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);
            ResultSet mockRs = mock(ResultSet.class);

            mockedDatabaseUtil.when(DatabaseUtil::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockRs);

            when(mockRs.next()).thenReturn(true).thenReturn(false);
            when(mockRs.getString("company_name")).thenReturn("All Companies");
            when(mockRs.getString("area_code")).thenReturn("PRISTINA");
            when(mockRs.getString("description")).thenReturn("Matches empty search");
            when(mockRs.getString("company_status")).thenReturn("ACTIVE"); // Add missing field


            List<Company> companies = CompanyRepository.searchCompanies("");
            assertFalse(companies.isEmpty());
            verify(mockStmt).setString(1,"%%");
        }
    }


}
