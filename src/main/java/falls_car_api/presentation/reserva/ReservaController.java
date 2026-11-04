package falls_car_api.presentation.reserva;

import falls_car_api.application.reserva.ReservaApplicationService;
import falls_car_api.application.reserva.ReservaRequestDTO;
import falls_car_api.application.reserva.ReservaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api/v1/reservas", "/api/reservas"})
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaApplicationService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> criar(@RequestBody @Valid ReservaRequestDTO requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.criar(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(reservaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(reservaService.buscarPorId(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(@PathVariable UUID id) {
        return ResponseEntity.ok(reservaService.cancelarReserva(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> atualizar(@PathVariable UUID id,
                                                        @RequestBody @Valid ReservaRequestDTO requestDto) {
        return ResponseEntity.ok(reservaService.atualizar(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        reservaService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
