package com.sidrerias.dto;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
}
