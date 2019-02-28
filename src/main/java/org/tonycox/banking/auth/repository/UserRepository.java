package org.tonycox.banking.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tonycox.banking.auth.model.UserDao;

@Repository
public interface UserRepository extends CrudRepository<UserDao, Long> {
    boolean existsByEmail(String email);
}
