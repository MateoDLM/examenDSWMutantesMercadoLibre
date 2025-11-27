package com.example.examenMutantesML.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estadísticas de verificaciones de ADN")
public class StatsResponse {

    @JsonProperty("count_mutant_dna")
    @Schema(description = "Cantidad de ADN mutante verificado")
    private long countMutantDna;

    @JsonProperty("count_human_dna")
    @Schema(description = "Cantidad de ADN humano verificado")
    private long countHumanDna;

    @Schema(description = "Ratio: mutantes / humanos")
    private double ratio;
}
