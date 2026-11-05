package falls_car_api.presentation.cadastro;

import falls_car_api.application.cadastro.AlterarDisponibilidadeMotoristaDTO;
import falls_car_api.application.cadastro.MotoristaApplicationService;
import falls_car_api.application.cadastro.MotoristaRequestDTO;
import falls_car_api.application.cadastro.MotoristaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/motoristas")
@RequiredArgsConstructor
public class MotoristaController {

    private final MotoristaApplicationService motoristaService;

    @PostMapping
    public ResponseEntity<MotoristaResponseDTO> criarMotorista(@RequestBody @Valid MotoristaRequestDTO requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(motoristaService.criarMotorista(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<MotoristaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(motoristaService.listarTodos());
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<MotoristaResponseDTO>> listarMotoristasDisponiveis(
            @RequestParam(required = false) String cidade) {
        return ResponseEntity.ok(motoristaService.listarMotoristasDisponiveis(cidade));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoristaResponseDTO> buscarMotoristaPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(motoristaService.buscarMotoristaPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoristaResponseDTO> atualizar(@PathVariable UUID id,
                                                          @RequestBody @Valid MotoristaRequestDTO requestDto) {
        return ResponseEntity.ok(motoristaService.atualizar(id, requestDto));
    }

    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<MotoristaResponseDTO> alterarDisponibilidadeMotorista(
            @PathVariable UUID id,
            @RequestBody @Valid AlterarDisponibilidadeMotoristaDTO dto) {
        return ResponseEntity.ok(motoristaService.alterarDisponibilidadeMotorista(id, dto.getIsDisponivel()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        motoristaService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
