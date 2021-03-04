package org.myapp.dev.core.user;

import org.myapp.dev.api.status.OperationStatus;
import org.myapp.dev.core.user.model.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<UserDto> createUser(UserDto userDetails);
    Optional<UserDto> getUserDetails(String userId);
    Optional<UserDto> updateUserDetails(String userId, UserDto userDetails);
    Optional<OperationStatus> deleteUserDetails(String userId, HttpServletRequest request);
    Optional<List<UserDto>> getAllUsers(int offset, int limit);
}
