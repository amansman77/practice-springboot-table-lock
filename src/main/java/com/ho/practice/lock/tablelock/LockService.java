package com.ho.practice.lock.tablelock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
public class LockService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final int MAX_RETRY_COUNT_S = 3;
    private static final long SLEEP_TIME_MS = 3000;

    @Autowired
    private final LockIsolateService lockIsolateService;
    
    public LockService(LockIsolateService lockIsolateService) {
        this.lockIsolateService = lockIsolateService;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean lock(String key) {
        try {
            lockIsolateService.lock(key);
        } catch (DataIntegrityViolationException e) {
            if (Boolean.TRUE.equals(lockIsolateService.isDeadLock(key))) {
                log.debug("is dead lock");
                lockIsolateService.forcelock(key);
                return Boolean.TRUE;
            }
            return this.retry(key, 1);
        }
        
        return Boolean.TRUE;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    private Boolean retry(String key, int retryCount) {
        log.debug("retry " + retryCount);
        try {
            Thread.sleep(SLEEP_TIME_MS);
        } catch (InterruptedException e) {
            return Boolean.FALSE;
        }

        if (retryCount > MAX_RETRY_COUNT_S) {
            return Boolean.FALSE;
        }
        try {
            lockIsolateService.lock(key);
        } catch (DataIntegrityViolationException e) {
            return this.retry(key, retryCount + 1);
        }
        
        return Boolean.TRUE;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void unLock(String key) {
        lockIsolateService.unLock(key);
    }
}
