package com.easypump.repository.role;


import com.easypump.model.role.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    Permission findOneByName(String name);
}
