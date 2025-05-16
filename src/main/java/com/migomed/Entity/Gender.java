package com.migomed.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender {
    @JsonProperty("мужской")
    МУЖСКОЙ,

    @JsonProperty("женский")
    ЖЕНСКИЙ,

    НЕ_УКАЗАНО;
}
