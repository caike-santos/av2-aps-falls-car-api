package falls_car_api.presentation.frota;

import falls_car_api.application.frota.*;
import falls_car_api.domain.shared.StatusAutomovel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/automoveis")
@RequiredArgsConstructor
public class AutomovelController {

    private final AutomovelApplicationService automovelService;

    @PostMapping
    public ResponseEntity<AutomovelResponseDTO> criar(@RequestBody @Valid AutomovelRequestDTO requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(automovelService.criar(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<AutomovelResponseDTO>> listarPorFiltro(
            @RequestParam(required = false) StatusAutomovel statusAutomovel,
            @RequestParam(required = false) UUID lojaAtualId,
            @RequestParam(required = false) String cidade) {
        return ResponseEntity.ok(automovelService.listarPorFiltro(statusAutomovel, lojaAtualId, cidade));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutomovelResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(automovelService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable UUID id, @RequestBody @Valid AutomovelRequestDTO requestDto) {
        automovelService.atualizar(id, requestDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AutomovelResponseDTO> alterarStatus(@PathVariable UUID id,
                                                              @RequestBody @Valid AlterarStatusAutomovelDTO dto) {
        return ResponseEntity.ok(automovelService.alterarStatus(id, dto));
    }

    @PatchMapping("/{id}/transferir-loja")
    public ResponseEntity<AutomovelResponseDTO> transferirLoja(@PathVariable UUID id,
                                                               @RequestBody @Valid TransferirLojaDTO dto) {
        return ResponseEntity.ok(automovelService.transferirLoja(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        automovelService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
