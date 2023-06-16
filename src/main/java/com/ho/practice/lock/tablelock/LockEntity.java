package com.ho.practice.lock.tablelock;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class LockEntity {

    private static final Integer EXPIRE_TIME = 10;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String key;

    @Column(updatable = false)
    private LocalDateTime createDateTime;

    
    protected LockEntity() {
    }
    public LockEntity(String key) {
        this.key = key;
        this.createDateTime = LocalDateTime.now();
    }
    public LockEntity(String key, LocalDateTime createDateTime) {
        this.key = key;
        this.createDateTime = createDateTime;
    }

    public String getKey() {
        return this.key;
    }
    public LocalDateTime getCreateDateTime() {
        return this.createDateTime;
    }
    public Boolean isDead() {
        LocalDateTime now = LocalDateTime.now();
        return this.createDateTime.plusSeconds(EXPIRE_TIME).isBefore(now);
    }
    public void extend() {
        this.createDateTime = LocalDateTime.now();
    }
}
