package com.example.examenMutantesML.service;

import com.example.examenMutantesML.entity.DnaRecord;
import com.example.examenMutantesML.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    // Datos de prueba
    private final String[] mutantDna = {"AAAA", "CCCC", "GGGG", "TTTT"};
    private final String[] humanDna = {"TAAT", "GCCG", "CGGC", "ATTA"};

    @Test
    @DisplayName("Debe analizar ADN mutante y guardarlo en DB")
    void testAnalyzeMutantDnaAndSave() {
        // ARRANGE (Preparar)
        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.empty());  // No existe en BD
        when(mutantDetector.isMutant(mutantDna))
                .thenReturn(true);  // Es mutante
        when(dnaRecordRepository.save(any(DnaRecord.class)))
                .thenReturn(new DnaRecord());  // Guardado exitoso

        // ACT (Actuar)
        boolean result = mutantService.analyzeDna(mutantDna);

        // ASSERT (Afirmar)
        assertTrue(result);

        // VERIFY (Verificar interacciones)
        verify(mutantDetector, times(1)).isMutant(mutantDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe analizar ADN humano y guardarlo en DB")
    void testAnalyzeHumanDnaAndSave() {
        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(humanDna))
                .thenReturn(false);  // Es humano
        when(dnaRecordRepository.save(any(DnaRecord.class)))
                .thenReturn(new DnaRecord());

        boolean result = mutantService.analyzeDna(humanDna);

        assertFalse(result);
        verify(mutantDetector, times(1)).isMutant(humanDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe retornar resultado cacheado si el ADN ya fue analizado")
    void testReturnCachedResultForAnalyzedDna() {
        // ARRANGE
        DnaRecord cachedRecord = new DnaRecord();
        cachedRecord.setMutant(true);
        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.of(cachedRecord));  // YA existe en BD

        // ACT
        boolean result = mutantService.analyzeDna(mutantDna);

        // ASSERT
        assertTrue(result);

        // VERIFY - NO debe llamar al detector ni guardar
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar hash consistente para el mismo ADN")
    void testConsistentHashGeneration() {
        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any()))
                .thenReturn(true);

        mutantService.analyzeDna(mutantDna);
        mutantService.analyzeDna(mutantDna);  // Mismo DNA otra vez

        // Debe buscar por el mismo hash ambas veces
        verify(dnaRecordRepository, times(2)).findByDnaHash(anyString());
    }

    @Test
    @DisplayName("Debe guardar registro con hash correcto")
    void testSavesRecordWithCorrectHash() {
        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(mutantDna))
                .thenReturn(true);

        mutantService.analyzeDna(mutantDna);

        verify(dnaRecordRepository).save(argThat(record ->
                record.getDnaHash() != null &&
                        record.getDnaHash().length() == 64 &&
                        record.isMutant()
        ));
    }
}