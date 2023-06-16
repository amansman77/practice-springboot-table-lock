package com.ho.practice.lock.tablelock;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LockRepository extends JpaRepository<LockEntity, Long> {

    Optional<LockEntity> findByKey(String key);

    void deleteByKey(String key);

}
