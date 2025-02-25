import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import ENUMS.Role;
import model.CreateUser;
import model.dto.UserDto;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import repository.UserRepository;
import service.PasswordHasher;
import service.UserService;
import java.time.LocalDateTime;


public class SignUpTest {
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @org.junit.Test
    public void testSignUpSuccessfulRegistration() {
        UserDto userDto = new UserDto("2", "Albin", "Arifaj", "albin@viviane.com", "albinn", "password", "password", Role.USER, LocalDateTime.now());
        String salt = "salt";
        String hashedPassword = "hashedPassword";
        try (MockedStatic<PasswordHasher> mockedPasswordHasher = mockStatic(PasswordHasher.class)) {
            mockedPasswordHasher.when(PasswordHasher::generateSalt).thenReturn(salt);
            mockedPasswordHasher.when(() -> PasswordHasher.generateSaltedHash(userDto.getPassword(), salt)).thenReturn(hashedPassword);
            try (MockedStatic<UserRepository> mockedUserRepository = mockStatic(UserRepository.class)) {
                mockedUserRepository.when(() -> UserRepository.insertUser(any(CreateUser.class))).thenReturn(true);
                boolean result = UserService.signUP(userDto);
                assertTrue(result);
            }
        }
    }
    @org.junit.Test
    public void testSignUpFailedRegistration() {
        UserDto userDto = new UserDto("3", "Albin", "Arifaj", "albin@viviane.com", "albin", "password", "password", Role.USER, LocalDateTime.now());
        String salt = "salt";
        String hashedPassword = "hashedPassword";
        try (MockedStatic<PasswordHasher> mockedPasswordHasher = mockStatic(PasswordHasher.class)) {
            mockedPasswordHasher.when(PasswordHasher::generateSalt).thenReturn(salt);
            mockedPasswordHasher.when(() -> PasswordHasher.generateSaltedHash(userDto.getPassword(), salt)).thenReturn(hashedPassword);
            try (MockedStatic<UserRepository> mockedUserRepository = mockStatic(UserRepository.class)) {
                mockedUserRepository.when(() -> UserRepository.insertUser(any(CreateUser.class))).thenReturn(false);
                boolean result = UserService.signUP(userDto);
                assertFalse(result);
            }
        }
    }
    @org.junit.Test
    public void testSignUpNullEmail() {
        UserDto userDto = new UserDto("4", "Albin", "Arifaj", null, "albin", "password", "password", Role.USER, LocalDateTime.now());
        String salt = "salt";
        String hashedPassword = "hashedPassword";
        try (MockedStatic<PasswordHasher> mockedPasswordHasher = mockStatic(PasswordHasher.class)) {
            mockedPasswordHasher.when(PasswordHasher::generateSalt).thenReturn("randomSalt");
            mockedPasswordHasher.when(() -> PasswordHasher.generateSaltedHash(userDto.getPassword(), salt)).thenReturn(hashedPassword);
            try (MockedStatic<UserRepository> mockedUserRepository = mockStatic(UserRepository.class)) {
                mockedUserRepository.when(() -> UserRepository.insertUser(any(CreateUser.class))).thenReturn(false);
                boolean result = UserService.signUP(userDto);
                assertFalse(result);
            }
        }
    }
    
    @org.junit.Test
    public void testSignUpHashingErrorThrowsException() {
        UserDto userDto = new UserDto("4", "Albin", "Arifaj", "albin@viviane.com", "albin", "password", "password", Role.USER, LocalDateTime.now());
        try (MockedStatic<PasswordHasher> mockedPasswordHasher = mockStatic(PasswordHasher.class)) {
            mockedPasswordHasher.when(PasswordHasher::generateSalt).thenReturn("randomSalt");
            mockedPasswordHasher.when(() -> PasswordHasher.generateSaltedHash(userDto.getPassword(), "randomSalt"))
                    .thenThrow(new RuntimeException("Hashing error"));
            assertThrows(RuntimeException.class, () -> {
                UserService.signUP(userDto);
            });
        }
    }
}

