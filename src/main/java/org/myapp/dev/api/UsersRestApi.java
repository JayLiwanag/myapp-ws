package org.myapp.dev.api;

import org.modelmapper.ModelMapper;
import org.myapp.dev.core.user.UserService;
import org.myapp.dev.core.user.model.dto.UserDto;
import org.myapp.dev.core.user.model.request.UserSignUpDetails;
import org.myapp.dev.core.user.model.response.UserDetailsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UsersRestApi {

    private final ModelMapper modelMapper = new ModelMapper();
    private final UserService userService;

    @Autowired
    public UsersRestApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE}
    )
    public ResponseEntity<UserDetailsRes> createUser(@Valid @RequestBody UserSignUpDetails userSignUpDetails) {
        UserDto userDto = modelMapper.map(userSignUpDetails, UserDto.class);

        Optional<UserDto> user = userService.createUser(userDto);

        UserDetailsRes userDetailsRes = modelMapper.map(user.get(), UserDetailsRes.class);

        if (user.isPresent())
            return ResponseEntity.status(HttpStatus.CREATED).body(userDetailsRes);
        else return ResponseEntity.badRequest().build();
    }

    @GetMapping (produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
    public ResponseEntity<List<UserDetailsRes>> fetchAllUsers(
                @RequestParam(value = "offset", defaultValue = "1") int offset,
                @RequestParam(value = "limit", defaultValue = "50") int limit) {

        Optional<List<UserDto>> allUsers = userService.getAllUsers(offset, limit);

        List<UserDetailsRes> returnVal =
                allUsers.get().stream()
                        .map(e -> modelMapper.map(e, UserDetailsRes.class))
                        .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(returnVal);
    }
}
