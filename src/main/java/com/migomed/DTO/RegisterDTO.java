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
    // "Сырой" номер паспорта, который будет хэширован
    private String passportNumber;
    private LocalDateTime dateOfBirth;
    // Флаг, который указывается пользователем, true – если работник, false – иначе
    private Boolean worker;
}
