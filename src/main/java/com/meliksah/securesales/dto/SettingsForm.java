package com.meliksah.securesales.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author mselvi
 * @Created 22.08.2023
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SettingsForm {

    @NotNull(message = "Enabled cannot be null or empty")
    private Boolean enabled;

    @NotNull(message = "Not Locked cannot be null or empty")
    private Boolean notLocked;
}
