package falls_car_api.presentation;
import falls_car_api.application.locacao.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/locacoes")
public class LocacaoController {
    private final LocacaoApplicationService locacaoService;
    public LocacaoController(LocacaoApplicationService locacaoService) {
        this.locacaoService = locacaoService;
    }
    @PostMapping("/efetivar")
    public ResponseEntity<LocacaoResponseDTO> efetivarLocacao(@RequestBody @Valid EfetivarLocacaoRequestDTO dto) {
        LocacaoResponseDTO response = locacaoService.efetivarLocacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LocacaoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(locacaoService.buscarPorId(id));
    }
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<LocacaoResponseDTO> finalizarLocacao(
            @PathVariable UUID id,
            @RequestBody @Valid FinalizarLocacaoDTO dto
    ) {
        return ResponseEntity.ok(locacaoService.finalizarLocacao(id, dto));
    }
}
