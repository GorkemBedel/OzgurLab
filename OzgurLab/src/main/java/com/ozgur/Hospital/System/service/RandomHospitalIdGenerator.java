package com.ozgur.Hospital.System.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomHospitalIdGenerator {


    private static final Long MIN_ID = 1000000L;
    private static final Long MAX_ID = 9999999L;
    private static final Long MIN_TC = 10000000000L;
    private static final Long MAX_TC = 99999999999L;
    private static Random random = new Random();
    private static Set<Long> usedIds = new HashSet<>();
    private static Set<Long> usedTCs = new HashSet<>();

    public static Long generateUniqueSevenDigitId() {
        Long id;
        do {
            id = MIN_ID + random.nextLong(MAX_ID - MIN_ID + 1);
        } while (usedIds.contains(id)); // Benzersizliği kontrol et
        usedIds.add(id);
        return id;
    }
    public static Long generateUniqueElevenDigitId() {
        Long tc;
        do {
            tc = MIN_TC + random.nextLong(MAX_TC - MIN_TC + 1);
        } while (usedTCs.contains(tc)); // Benzersizliği kontrol et
        usedTCs.add(tc);
        return tc;
    }
}
