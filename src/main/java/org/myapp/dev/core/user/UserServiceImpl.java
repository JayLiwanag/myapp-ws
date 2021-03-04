package org.myapp.dev.core.user;

import org.modelmapper.ModelMapper;
import org.myapp.dev.api.exception.ErrorMessage;
import org.myapp.dev.api.exception.UserServiceException;
import org.myapp.dev.api.status.OperationStatus;
import org.myapp.dev.api.status.StatusMessage;
import org.myapp.dev.core.user.dao.PrivilegeRepository;
import org.myapp.dev.core.user.dao.RoleRepository;
import org.myapp.dev.core.user.dao.UserRepository;
import org.myapp.dev.core.user.model.PrivilegeEntity;
import org.myapp.dev.core.user.model.Role;
import org.myapp.dev.core.user.model.RoleEntity;
import org.myapp.dev.core.user.model.dto.UserDto;
import org.myapp.dev.core.user.model.UserEntity;
import org.myapp.dev.shared.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, BCryptPasswordEncoder bCryptPasswordEncoder, Utils utils) {
        this.userRepository = userRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public Optional<UserDto> createUser(UserDto userDetails) {
        Optional<UserEntity> userCheck = userRepository.findByEmail(userDetails.getEmail());

        if (!userCheck.isEmpty())
            throw new UserServiceException(ErrorMessage.EMAIL_ALREADY_EXIST.getErrorMessage());

        Set<RoleEntity> roleSet = new HashSet<>();

        Optional<RoleEntity> roles = roleRepository.findByName(Role.ROLE_USER.name());

        if (roles.isPresent())
            roleSet.add(roles.get());

        userDetails.setUserId(utils.generateUserId());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        UserEntity data = modelMapper.map(userDetails, UserEntity.class);
        data.setRoles(roleSet);

        UserEntity user = userRepository.save(data);

        return Optional.ofNullable(modelMapper.map(user, UserDto.class));
    }

    @Override
    public Optional<UserDto> getUserDetails(String userId) {
        Optional<UserEntity> user = userRepository.findByUserId(userId);

        if (user.isEmpty())
            throw new UserServiceException(ErrorMessage.USERNAME_NOT_EXIST.getErrorMessage(), HttpStatus.NOT_FOUND);

        return Optional.ofNullable(modelMapper.map(user.get(), UserDto.class));
    }

    @Override
    public Optional<UserDto> updateUserDetails(String userId, UserDto userDetails) {
        Optional<UserEntity> user = userRepository.findByUserId(userId);

        if (user.isEmpty())
            throw new UserServiceException(ErrorMessage.USERNAME_NOT_EXIST.getErrorMessage(), HttpStatus.NOT_FOUND);

        UserEntity newUserDetails = user.get();

        // Set details to update.
        newUserDetails.setFirstName(userDetails.getFirstName());
        newUserDetails.setLastName(userDetails.getLastName());

        UserEntity savedDetails = userRepository.save(newUserDetails);

        return Optional.ofNullable(modelMapper.map(savedDetails, UserDto.class));
    }

    @Override
    public Optional<OperationStatus> deleteUserDetails(String userId, HttpServletRequest request) {

        try {
            Optional<UserEntity> user = userRepository.findByUserId(userId);

            if (user.isEmpty())
                throw new UserServiceException(ErrorMessage.USERNAME_NOT_EXIST.getErrorMessage(),
                        StatusMessage.FAILED.getStatusMessage(),
                        HttpStatus.NOT_FOUND);

            userRepository.delete(user.get());

            return Optional.of( new OperationStatus (
                    LocalDateTime.now(),
                    StatusMessage.MSG_USER_DELETE_OPERATION.getStatusMessage(),
                    StatusMessage.SUCCESS.getStatusMessage(),
                    request.getRequestURI()
            ));
        }
        catch (Exception e) { throw new UserServiceException(ErrorMessage.USERNAME_NOT_EXIST.getErrorMessage(),
                StatusMessage.FAILED.getStatusMessage(),
                HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<List<UserDto>> getAllUsers(int offset, int limit) {

        if (offset > 0)
            offset--;


        Pageable pageable = PageRequest.of(offset, limit );

//        Page<UserEntity> users = userRepository.findAll(pageable);
        Page<UserEntity> users = userRepository.findAllUsersOnDB(pageable);

        List<UserDto> returnVal =
                users.getContent().stream()
                    .map(e -> modelMapper.map(e, UserDto.class))
                    .collect(Collectors.toList());

        return Optional.ofNullable(returnVal);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new UserServiceException(ErrorMessage.USERNAME_NOT_EXIST.getErrorMessage());

//        System.out.println("Roles : " + user.get().getRoles());

        return new User(user.get().getUserId(),
                user.get().getEncryptedPassword(),
                user.get().getActive(),
                true,
                true,
                true,
                getAuthorities(user.get().getRoles()));
    }


    private Collection<? extends SimpleGrantedAuthority> getAuthorities(final Collection<RoleEntity> roles) {
        // create list of authorities
        List<SimpleGrantedAuthority> grantedAuthorities = getGrantedAuthorities(roles);

        // add roles to the list
        roles.stream().forEach(e -> grantedAuthorities.add(new SimpleGrantedAuthority(e.getName())));

        return grantedAuthorities;
    }

    private List<SimpleGrantedAuthority> getGrantedAuthorities(final Collection<RoleEntity> roles) {
        return roles.stream()
                .map(role -> role.getPrivileges())
                .flatMap(privileges -> privileges.stream())
                .distinct()
                .map(e -> new SimpleGrantedAuthority(e.getName())).collect(Collectors.toList());
    }
}
