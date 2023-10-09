package users;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@AllArgsConstructor
public class UserNewData {
    private String email;
    private String name;

    public static UserNewData generateNewUserDataRandom(){
        String email = RandomStringUtils.randomAlphabetic(6) + "@yandex.ru";
        String name = RandomStringUtils.randomAlphabetic(6);
        return new UserNewData(email, name);
    }
}