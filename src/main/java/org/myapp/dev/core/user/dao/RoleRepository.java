package org.myapp.dev.core.user.dao;

import org.myapp.dev.core.user.model.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends CrudRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}
