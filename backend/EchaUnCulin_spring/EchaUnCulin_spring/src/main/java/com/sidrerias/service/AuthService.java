package com.sidrerias.service;

import com.sidrerias.dto.TokenResponse;
import com.sidrerias.dto.UserRequest;
import com.sidrerias.dto.UserResponse;
import com.sidrerias.modelo.Usuario;

public interface AuthService {
    UserResponse createUser(Usuario userRequest);
    UserResponse loadUser(UserRequest userRequest);
}
