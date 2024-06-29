package feedaggregator.dto;

import feedaggregator.module.User;

public class UserDto {
    public String username;
    public String email;

    public static UserDto fromEntity(User user) {
        UserDto userDto = new UserDto();
        userDto.username = user.getUsername();
        userDto.email = user.getEmail();
        return userDto;
    }
}
