package org.myapp.dev.api;

import org.modelmapper.ModelMapper;
import org.myapp.dev.api.status.OperationStatus;
import org.myapp.dev.core.user.UserService;
import org.myapp.dev.core.user.model.dto.UserDto;
import org.myapp.dev.core.user.model.request.UserUpdateDetails;
import org.myapp.dev.core.user.model.response.UserDetailsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = "/profile/{userId}")
public class UserProfileRestApi {


    private final ModelMapper modelMapper = new ModelMapper();
    private final UserService userService;

    @Autowired
    public UserProfileRestApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping (
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE}
    )
    public ResponseEntity<UserDetailsRes> getUserProfile (@PathVariable String userId) {

        Optional<UserDto> userDetails = userService.getUserDetails(userId);

        return userDetails
                .map(userDto -> ResponseEntity.ok().body(modelMapper.map(userDto, UserDetailsRes.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping (
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE}
    )
    public ResponseEntity<UserDetailsRes> updateUserProfile (@PathVariable String userId, @Valid @RequestBody UserUpdateDetails userUpdateDetails) {

        UserDto userDetails = modelMapper.map(userUpdateDetails, UserDto.class);

        Optional<UserDto> updatedUserDetails = userService.updateUserDetails(userId, userDetails);

        return updatedUserDetails
                .map(userDto -> ResponseEntity.ok().body(modelMapper.map(userDto, UserDetailsRes.class)))
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }

    @DeleteMapping (
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE}
    )
    public ResponseEntity<OperationStatus> deleteUserProfile (@PathVariable String userId, HttpServletRequest request) {

        Optional<OperationStatus> operationStatus = userService.deleteUserDetails(userId, request);

        return operationStatus
                .map(OperationStatus -> ResponseEntity.ok().body(OperationStatus))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
