package dev.lycheevm.productrestfulspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductDto(@NotBlank String name, @NotNull BigDecimal value) {
}
