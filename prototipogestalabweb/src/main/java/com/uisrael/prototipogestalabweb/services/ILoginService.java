package com.uisrael.prototipogestalabweb.services;

import com.uisrael.prototipogestalabweb.model.dto.request.LoginRequestDto;
import com.uisrael.prototipogestalabweb.model.dto.response.LoginResponseDto;

public interface ILoginService {
	
	LoginResponseDto login(LoginRequestDto credenciales);

}
