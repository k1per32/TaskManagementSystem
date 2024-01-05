package com.k1per32.TaskManagementSystem.mapper;

import com.k1per32.TaskManagementSystem.dto.UserDto;
import com.k1per32.TaskManagementSystem.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User convertToUser(UserDto userDto);
    UserDto convertToUserDto(User user);
}