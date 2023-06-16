package com.ho.practice.lock.tablelock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.persistence.EntityManager;

@SpringBootTest
@DisplayName("락 도메인을 담당하는 서비스입니다.")
public class LockServiceTest {

    @Autowired
    private LockService lockService;

    @Autowired
    private LockRepository lockRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("락에 진입합니다.")
    void testInLock() {
        String key = "key-230616-2307";
        Boolean inLock = lockService.lock(key);
        
        Optional<LockEntity> findLockEntityOptional = this.lockRepository.findByKey(key);

        assertTrue(inLock);
        assertTrue(findLockEntityOptional.isPresent());
        assertEquals("key-230616-2307", findLockEntityOptional.get().getKey());
        assertNotNull(findLockEntityOptional.get().getCreateDateTime());
    }

    @Test
    @DisplayName("동일한 키를 가진 락이 존재하면 진입하지 못합니다.")
    void testLock() {
        String key = "key-230616-2310";

        lockRepository.save(new LockEntity(key));
        entityManager.clear();

        Boolean inLock = lockService.lock(key);
        
        assertFalse(inLock);
    }

    @Test
    @DisplayName("데드락은 무시하고 락에 진입합니다.")
    void testIgnoreDeadLock() {
        String key = "key-230616-0002";

        lockRepository.save(new LockEntity(key, LocalDateTime.now().minusMinutes(1)));
        entityManager.clear();

        Boolean inLock = lockService.lock(key);
        
        assertTrue(inLock);
    }

    @Test
    @DisplayName("락을 해제합니다.")
    void testUnLock() {
        String key = "key-230617-0000";

        lockRepository.save(new LockEntity(key));
        entityManager.clear();

        lockService.unLock(key);
        
        Optional<LockEntity> findLockEntityOptional = this.lockRepository.findByKey(key);

        assertFalse(findLockEntityOptional.isPresent());
    }
    
}
