package falls_car_api.presentation.auth;

import falls_car_api.application.auth.AuthApplicationService;
import falls_car_api.application.auth.LoginRequestDTO;
import falls_car_api.application.auth.LoginResponseDTO;
import falls_car_api.application.auth.RegistroRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthApplicationService authService;
    public AuthController(AuthApplicationService authService) {
        this.authService = authService;
    }
    @PostMapping("/registrar")
    public ResponseEntity<LoginResponseDTO> registrar(@RequestBody @Valid RegistroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(dto));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.autenticar(dto));
    }
}
