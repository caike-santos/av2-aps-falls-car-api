package falls_car_api.application.loja;

import falls_car_api.domain.loja.ILojaRepository;
import falls_car_api.domain.loja.Loja;
import falls_car_api.domain.loja.VoCoordenada;
import falls_car_api.domain.shared.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LojaApplicationService {

    private final ILojaRepository lojaRepository;

    @Transactional
    public void criar(LojaRequestDTO requestDto) { //[cite: 2]
        Loja loja = new Loja();
        loja.setId(UUID.randomUUID()); // Gera um UUID padronizado[cite: 3]
        atualizarDadosLoja(loja, requestDto);
        lojaRepository.salvar(loja); //[cite: 2]
    }
    
    public LojaResponseDTO buscarPorId(UUID id) { //[cite: 2]
        Loja loja = lojaRepository.buscarPorId(id) //[cite: 2]
                .orElseThrow(() -> new DomainException("Loja não encontrada."));
        return converterParaDto(loja);
    }
    
    public List<LojaResponseDTO> listarTodos() { //[cite: 2]
        return lojaRepository.listarTodos().stream() //[cite: 2]
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void atualizar(UUID id, LojaRequestDTO requestDto) { //[cite: 2]
        Loja loja = lojaRepository.buscarPorId(id)
                .orElseThrow(() -> new DomainException("Loja não encontrada."));
        atualizarDadosLoja(loja, requestDto);
        lojaRepository.atualizar(loja); //[cite: 2]
    }
    
    @Transactional
    public void remover(UUID id) { //[cite: 2]
        lojaRepository.removerPorId(id); //[cite: 2]
    }

    // Métodos auxiliares privados para conversão (Evitam código repetido)
    private void atualizarDadosLoja(Loja loja, LojaRequestDTO dto) {
        loja.setNome(dto.getNome());
        loja.setTipo(dto.getTipoLoja());
        loja.setCidade(dto.getCidade());
        loja.setEstado(dto.getEstado());
        loja.setEnderecoCompleto(dto.getEnderecoCompleto());
        loja.setCoordenada(new VoCoordenada(dto.getLatitude(), dto.getLongitude()));
        loja.setIsAtivo(dto.getIsAtiva()); //[cite: 3]
    }

    private LojaResponseDTO converterParaDto(Loja loja) {
        LojaResponseDTO dto = new LojaResponseDTO();
        dto.setId(loja.getId()); //[cite: 3]
        dto.setNome(loja.getNome());
        dto.setTipoLoja(loja.getTipo());
        dto.setCidade(loja.getCidade());
        dto.setEstado(loja.getEstado());
        dto.setEnderecoCompleto(loja.getEnderecoCompleto());
        
        if (loja.getCoordenada() != null) {
            dto.setLatitude(loja.getCoordenada().latitude());
            dto.setLongitude(loja.getCoordenada().longitude());
        }
        
        dto.setIsAtiva(loja.getIsAtivo()); //[cite: 3]
        return dto;
    }
}