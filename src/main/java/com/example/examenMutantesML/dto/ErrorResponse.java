package com.example.examenMutantesML.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura estándar de respuesta de error")
public class ErrorResponse {
    @JsonProperty("error")
    @Schema(description = "Tipo de error", example = "Bad Request")
    private String error;

    @JsonProperty("message")
    @Schema(description = "Mensaje detallado", example = "Secuencia de ADN inválida")
    private String message;
}
