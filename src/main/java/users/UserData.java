package users;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@AllArgsConstructor
public class UserData {
    private String email;
    private String password;
    private String name;

    public static UserData generateUserRandom() {
        String email = RandomStringUtils.randomAlphabetic(6) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(6);
        String name = RandomStringUtils.randomAlphabetic(6);
        return new UserData(email, password, name);
    }
}
