package com.migomed.DTO;

import com.migomed.Entity.Gender;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RegisterDTO {
    private String surname;
    private String name;
    private String patronymic;
    private String passportNumber;
    private LocalDateTime dateOfBirth;
    private Boolean worker;
    private Gender gender;
}
