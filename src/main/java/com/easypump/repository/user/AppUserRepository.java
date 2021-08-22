package com.easypump.repository.user;

import com.easypump.model.common.AuditEntity;
import com.easypump.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<User, Integer> {

    User findOneByEmailAndRecordStatus(String email, AuditEntity.RecordStatus recordStatus);

    User findOneByIdAndRecordStatus(Integer id, AuditEntity.RecordStatus recordStatus);

    @Query(value = "SELECT * FROM app_user r WHERE r.record_status='ACTIVE' AND r.role_id =:roleId",
            nativeQuery = true)
    List<User> findByRoleId(Integer roleId);

    @Query(value = "SELECT * FROM app_user r WHERE r.record_status='ACTIVE' AND r.id =:id AND r.account_id = :accountId",
            nativeQuery = true)
    Optional<User> findByIdAndAccountId(Integer id, Integer accountId);
}
