package com.easypump.service.role;

import com.easypump.dto.ActionResponse;
import com.easypump.dto.role.PermissionDto;
import com.easypump.dto.role.RoleDto;
import com.easypump.infrastructure.ErrorMessages;
import com.easypump.infrastructure.PowerValidator;
import com.easypump.infrastructure.exception.BadRequestException;
import com.easypump.model.common.AuditEntity;
import com.easypump.model.role.Permission;
import com.easypump.model.role.PermissionEnum;
import com.easypump.model.role.Role;
import com.easypump.model.user.User;
import com.easypump.repository.role.PermissionRepository;
import com.easypump.repository.role.RoleRepository;
import com.easypump.repository.user.AppUserRepository;
import com.easypump.service.role.handler.RoleCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RoleCommandService implements RoleCommandHandler {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final AppUserRepository appUserRepository;


    @Autowired
    public RoleCommandService(PermissionRepository permissionRepository, RoleRepository roleRepository, AppUserRepository appUserRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.appUserRepository = appUserRepository;
    }


    @Override
    @Transactional
    public ActionResponse createRole(RoleDto roleDto) {
        roleDto.isValid();
        List<Role> existingRoles = roleRepository.findAllByName(roleDto.getName());
        PowerValidator.isEmpty(existingRoles, String.format(ErrorMessages.ENTITY_ALREADY_EXISTS, Role.class.getSimpleName(), "name"));
        List<Permission> permissions = validatePermissions(roleDto);
        Role role = new Role(roleDto.getName(), roleDto.getDescription(), permissions);
        role = roleRepository.save(role);
        return new ActionResponse(role.getId());
    }

    @Override
    @Transactional
    public ActionResponse updateRole(RoleDto roleDto, Integer roleId) {
        roleDto.isValid();
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new BadRequestException(String.format(ErrorMessages.ENTITY_DOES_NOT_EXISTS, Role.class.getSimpleName(), "id")));
        if (!roleDto.getName().equals(role.getName())) {
            List<Role> existingRoles = roleRepository.findAllByName(roleDto.getName());
            PowerValidator.isEmpty(existingRoles, String.format(ErrorMessages.ENTITY_ALREADY_EXISTS, Role.class.getSimpleName(), "name"));
        }
        List<Permission> permissions = validatePermissions(roleDto);
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());
        role.setPermissions(permissions);
        roleRepository.save(role);
        return new ActionResponse(roleId);
    }

    @Override
    public ActionResponse removeRole(Integer roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new BadRequestException(String.format(ErrorMessages.ENTITY_DOES_NOT_EXISTS, Role.class.getSimpleName(), "id")));
        List<User> users = appUserRepository.findByRoleId(roleId);
        PowerValidator.isEmpty(users, ErrorMessages.ROLE_IS_IN_USE);
        role.setStatus(AuditEntity.RecordStatus.DELETED);
        roleRepository.save(role);
        return new ActionResponse(roleId);
    }


    private List<Permission> validatePermissions(RoleDto roleDto) {
        List<Permission> permissions = new ArrayList<>();
        if (roleDto.isSuperUser()) {
            permissions.add(permissionRepository.findOneByName(PermissionEnum.SUPER_PERMISSION.name()));
        } else {
            List<Integer> permissionIds = roleDto.getPermissions().stream().map(PermissionDto::getId).collect(Collectors.toList());
            permissions = permissionRepository.findAllById(permissionIds);
        }
        PowerValidator.notEmpty(permissions, ErrorMessages.PERMISSIONS_REQUIRED_FOR_ROLE);
        return permissions;
    }
}
