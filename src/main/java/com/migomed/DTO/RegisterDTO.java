package com.migomed.DTO;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {
    private String surname;
    private String name;
    private String patronymic;
    private String passportNumber;
    private LocalDateTime dateOfBirth;
    private Boolean worker;
}
