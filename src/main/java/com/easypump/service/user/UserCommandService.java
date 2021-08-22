package com.easypump.service.user;

import com.easypump.dto.ActionResponse;
import com.easypump.dto.user.UserDto;
import com.easypump.infrastructure.AppContext;
import com.easypump.infrastructure.ApplicationConstants;
import com.easypump.infrastructure.ErrorMessages;
import com.easypump.infrastructure.PowerValidator;
import com.easypump.infrastructure.exception.BadRequestException;
import com.easypump.model.common.AuditEntity;
import com.easypump.model.role.Role;
import com.easypump.model.user.User;
import com.easypump.repository.role.RoleRepository;
import com.easypump.repository.user.AppUserRepository;
import com.easypump.service.user.handler.UserCommandHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.Date;

@Service
public class UserCommandService implements UserCommandHandler {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserCommandService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder,
                              RoleRepository roleRepository) {

        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public ActionResponse createUser(UserDto userDto) {
        userDto.isValid();
        PowerValidator.notNull(userDto.getRole().getId(), String.format(ErrorMessages.ENTITY_REQUIRED, "role ID"));
        Role role = roleRepository.findById(userDto.getRole().getId()).orElseThrow(() ->
                new BadRequestException(String.format(ErrorMessages.ENTITY_DOES_NOT_EXISTS, Role.class.getSimpleName(), "ID")));
        User user = userDto.toUserEntity();
        user.setRole(role);
        user.setRecordStatus(AuditEntity.RecordStatus.PENDING_PASSWORD_RESET);
        AppContext.stamp(user);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        appUserRepository.save(user);
        return new ActionResponse(user.getId());
    }

    @Override
    public ActionResponse updateUser(UserDto userDto, Integer id) {
        userDto.isValid();
        User user = appUserRepository.findById(id).orElseThrow(() ->
                new BadRequestException(String.format(ErrorMessages.ENTITY_DOES_NOT_EXISTS, "User", "ID")));
        PowerValidator.notNull(userDto.getRole().getId(), String.format(ErrorMessages.ENTITY_REQUIRED, "role ID"));
        Role role = roleRepository.findById(userDto.getRole().getId()).orElseThrow(() ->
                new BadRequestException(String.format(ErrorMessages.ENTITY_DOES_NOT_EXISTS, Role.class.getSimpleName(), "ID")));
        user.setRole(role);
        AppContext.stamp(user);
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        appUserRepository.save(user);
        return new ActionResponse(user.getId());
    }

    @Override
    public ActionResponse closeUser(Integer userId) {
        PowerValidator.notNull(userId, String.format(ErrorMessages.ENTITY_REQUIRED, "User ID"));
        User user = appUserRepository.findById(userId).orElseThrow(() ->
                new BadRequestException(String.format(ErrorMessages.ENTITY_DOES_NOT_EXISTS, "User", "ID")));
        user.setRecordStatus(AuditEntity.RecordStatus.CLOSED);
        return new ActionResponse(userId);
    }
    public static String generateAuthenticationToken(User User) {
        String expirationTime = ApplicationConstants.JWT_EXPIRATION_TIME_IN_MINUTES;
        Date expiryDate = new Date(System.currentTimeMillis() + Long.parseLong(expirationTime) * 60 * 1000);
        String secretKey = Base64.getEncoder().encodeToString(ApplicationConstants.JWT_SECRET_KEY.getBytes());
        Claims claims = Jwts.claims().setSubject(User.getEmail());
        claims.put(ApplicationConstants.APP_USER_ID, User.getId());
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secretKey).setExpiration(expiryDate).compact();
    }
}
