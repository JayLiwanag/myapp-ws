package org.myapp.dev.core.user.dao;

import org.myapp.dev.core.user.model.PrivilegeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends CrudRepository<PrivilegeEntity, Long> {
    Optional<PrivilegeEntity> findByName(String name);
}
