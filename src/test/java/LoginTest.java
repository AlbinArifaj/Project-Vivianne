import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ENUMS.Role;
import model.User;
import model.dto.LoginUserDto;
import model.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import repository.UserRepository;
import service.PasswordHasher;
import service.UserService;

import java.time.LocalDateTime;

public class LoginTest {

  @Test
  public void testLoginSuccessful() {
    LoginUserDto loginUserDto = new LoginUserDto("adea", "adea123");
    String salt = "randomSalt";
    String hashedPassword = "hashedPassword";

    try (MockedStatic<UserRepository> mockedUserRepository = mockStatic(UserRepository.class);
         MockedStatic<PasswordHasher> mockedPasswordHasher = mockStatic(PasswordHasher.class)) {

      mockedUserRepository.when(() -> UserRepository.getByUsername(loginUserDto.getUsername()))
        .thenReturn(new User("1", "Adea", "Gerguri", loginUserDto.getUsername(), "adea.gerguri", salt, hashedPassword, Role.USER));

      mockedPasswordHasher.when(() -> PasswordHasher.compareSaltedHash(loginUserDto.getPassword(), salt, hashedPassword))
        .thenReturn(true);

      boolean result = UserService.login(loginUserDto);
      assertTrue(result);
    }
  }
  @Test
  public void testLoginFailedInvalidPassword() {
    LoginUserDto loginUserDto = new LoginUserDto("adea", "wrongPassword");
    String salt = "randomSalt";
    String correctHashedPassword = "correctHashedPassword";
    String wrongHashedPassword = "wrongHashedPassword";

    try (MockedStatic<UserRepository> mockedUserRepository = mockStatic(UserRepository.class);
         MockedStatic<PasswordHasher> mockedPasswordHasher = mockStatic(PasswordHasher.class)) {

      mockedUserRepository.when(() -> UserRepository.getByUsername(loginUserDto.getUsername()))
        .thenReturn(new User("1", "Adea", "Gerguri", loginUserDto.getUsername(), "adeagerguri1@gmail.com", salt, correctHashedPassword, Role.USER));

      mockedPasswordHasher.when(() -> PasswordHasher.compareSaltedHash(loginUserDto.getPassword(), salt, wrongHashedPassword))
        .thenReturn(false);

      boolean result = UserService.login(loginUserDto);
      assertFalse(result);
    }
  }


  @Test
  public void testLoginFailedUserNotFound() {
    LoginUserDto loginUserDto = new LoginUserDto("nonexistentUser", "password");

    try (MockedStatic<UserRepository> mockedUserRepository = mockStatic(UserRepository.class)) {
      mockedUserRepository.when(() -> UserRepository.getByUsername(loginUserDto.getUsername()))
        .thenReturn(null);

      boolean result = UserService.login(loginUserDto);
      assertFalse(result);
    }
  }

  @Test
  public void testLoginHashingErrorThrowsException() {
    LoginUserDto loginUserDto = new LoginUserDto("adea", "password");
    String salt = "randomSalt";
    String correctHashedPassword = "correctHashedPassword";

    try (MockedStatic<UserRepository> mockedUserRepository = mockStatic(UserRepository.class);
         MockedStatic<PasswordHasher> mockedPasswordHasher = mockStatic(PasswordHasher.class)) {

      mockedUserRepository.when(() -> UserRepository.getByUsername(loginUserDto.getUsername()))
        .thenReturn(new User("1", "Adea", "Gerguri", loginUserDto.getUsername(), "adeagerguri1@gmail.com", salt, correctHashedPassword, Role.USER));

      mockedPasswordHasher.when(() -> PasswordHasher.compareSaltedHash(loginUserDto.getPassword(), salt,correctHashedPassword))
        .then(invocation -> {
          System.out.println("Mocked PasswordHasher.generateSaltedHash() is called");
          throw new RuntimeException("Hashing error");
        });

      assertThrows(RuntimeException.class, () -> UserService.login(loginUserDto));
    }
  }
}
