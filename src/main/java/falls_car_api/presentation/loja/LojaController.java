package falls_car_api.presentation.loja;

import falls_car_api.application.loja.LojaApplicationService;
import falls_car_api.application.loja.LojaRequestDTO;
import falls_car_api.application.loja.LojaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lojas")
@RequiredArgsConstructor
public class LojaController {

    private final LojaApplicationService lojaService;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid LojaRequestDTO requestDto) {
        lojaService.criar(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LojaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(lojaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<LojaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(lojaService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable UUID id, @RequestBody @Valid LojaRequestDTO requestDto) {
        lojaService.atualizar(id, requestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        lojaService.remover(id);
        return ResponseEntity.noContent().build();
    }
}