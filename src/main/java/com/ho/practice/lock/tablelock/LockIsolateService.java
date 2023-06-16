package com.ho.practice.lock.tablelock;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
public class LockIsolateService {

    @Autowired
    private LockRepository lockRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void lock(String key) {
        lockRepository.save(new LockEntity(key));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void unLock(String key) {
        lockRepository.deleteByKey(key);
    }

    public Boolean isDeadLock(String key) {
        Optional<LockEntity> findLockEntityOptional = lockRepository.findByKey(key);
        return findLockEntityOptional.isPresent() && findLockEntityOptional.get().isDead();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void forcelock(String key) {
        Optional<LockEntity> findLockEntityOptional = lockRepository.findByKey(key);
        if (findLockEntityOptional.isPresent()) {
            findLockEntityOptional.get().extend();
        }
    }

}
