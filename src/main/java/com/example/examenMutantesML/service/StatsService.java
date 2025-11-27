package com.example.examenMutantesML.service;

import com.example.examenMutantesML.dto.StatsResponse;
import com.example.examenMutantesML.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository repository;

    public StatsResponse getStats() {
        long countMutant = repository.countByIsMutant(true);
        long countHuman = repository.countByIsMutant(false);
        double ratio = countHuman == 0 ?
                (countMutant > 0 ? countMutant : 0.0) :
                (double) countMutant / countHuman;
        return new StatsResponse(countMutant, countHuman, ratio);
    }

//    public StatsResponse getStats() {
//            List<DnaRecord> allRecords = repository.findAll();
//            long humanCount = allRecords.stream()
//                    .filter(x -> !x.isMutant())
//                    .count();
//            long mutantCount = allRecords.stream()
//                    .filter(DnaRecord::isMutant)
//                    .count();
//            double ratio;
//            if (humanCount == 0) ratio = 0.0;
//            else ratio = (double)mutantCount / (humanCount);
//
//            return new StatsResponse(mutantCount, humanCount, ratio);
//    }
}
