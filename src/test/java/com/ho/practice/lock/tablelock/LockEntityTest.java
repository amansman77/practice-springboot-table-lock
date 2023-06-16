package com.ho.practice.lock.tablelock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LockEntityTest {

    @Test
    @DisplayName("10초가 지나지 않으면 죽지 않습니다.")
    void testIsDead() {
        LockEntity lockEntity = new LockEntity("key");

        assertTrue(lockEntity.getCreateDateTime().isBefore(LocalDateTime.now().plusSeconds(10)));
        assertFalse(lockEntity.isDead());
    }
}
