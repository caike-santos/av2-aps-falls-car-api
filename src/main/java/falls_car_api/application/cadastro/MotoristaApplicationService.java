package falls_car_api.application.cadastro;

import falls_car_api.domain.cadastro.IMotoristaRepository;
import falls_car_api.domain.cadastro.Motorista;
import falls_car_api.domain.loja.ILojaRepository;
import falls_car_api.domain.loja.Loja;
import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MotoristaApplicationService {

    private final IMotoristaRepository motoristaRepository;
    private final ILojaRepository lojaRepository;

    @Transactional
    public MotoristaResponseDTO criarMotorista(MotoristaRequestDTO requestDto) {
        String cpf = somenteDigitos(requestDto.getCpf());
        if (motoristaRepository.buscarPorCpf(cpf).isPresent()) {
            throw new DomainException("Já existe um motorista cadastrado com este CPF.");
        }
        validarLojaBase(requestDto.getLojaBaseId());

        Motorista motorista = new Motorista();
        motorista.setId(UUID.randomUUID());
        motorista.setIsDisponivel(true);
        preencherDados(motorista, requestDto, cpf);

        motoristaRepository.salvar(motorista);
        return converterParaDto(motorista);
    }

    public MotoristaResponseDTO buscarMotoristaPorId(UUID id) {
        return converterParaDto(buscarOuFalhar(id));
    }

    public List<MotoristaResponseDTO> listarTodos() {
        List<MotoristaResponseDTO> resposta = new ArrayList<>();
        for (Motorista motorista : motoristaRepository.listarTodos()) {
            resposta.add(converterParaDto(motorista));
        }
        return resposta;
    }

    public List<MotoristaResponseDTO> listarMotoristasDisponiveis(String cidade) {
        Collection<UUID> lojasBaseIds = null;
        if (cidade != null && !cidade.isBlank()) {
            String cidadeNormalizada = normalizar(cidade);
            lojasBaseIds = new ArrayList<>();
            for (Loja loja : lojaRepository.listarTodos()) {
                if (loja.getCidade() != null && normalizar(loja.getCidade()).equals(cidadeNormalizada)) {
                    lojasBaseIds.add(loja.getId());
                }
            }
        }

        List<MotoristaResponseDTO> resposta = new ArrayList<>();
        for (Motorista motorista : motoristaRepository.listarDisponiveisPorLojas(lojasBaseIds)) {
            resposta.add(converterParaDto(motorista));
        }
        return resposta;
    }

    @Transactional
    public MotoristaResponseDTO alterarDisponibilidadeMotorista(UUID id, Boolean isDisponivel) {
        Motorista motorista = buscarOuFalhar(id);
        if (Boolean.TRUE.equals(isDisponivel)) {
            motorista.liberarMotorista();
        } else {
            motorista.alocarMotorista();
        }
        motoristaRepository.atualizar(motorista);
        return converterParaDto(motorista);
    }

    @Transactional
    public MotoristaResponseDTO atualizar(UUID id, MotoristaRequestDTO requestDto) {
        Motorista motorista = buscarOuFalhar(id);
        String cpf = somenteDigitos(requestDto.getCpf());

        motoristaRepository.buscarPorCpf(cpf).ifPresent(outro -> {
            if (!outro.getId().equals(id)) {
                throw new DomainException("Já existe outro motorista cadastrado com este CPF.");
            }
        });
        validarLojaBase(requestDto.getLojaBaseId());

        preencherDados(motorista, requestDto, cpf);
        motoristaRepository.atualizar(motorista);
        return converterParaDto(motorista);
    }

    @Transactional
    public void remover(UUID id) {
        buscarOuFalhar(id);
        motoristaRepository.removerPorId(id);
    }

    private Motorista buscarOuFalhar(UUID id) {
        return motoristaRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Motorista não encontrado."));
    }

    private void validarLojaBase(UUID lojaBaseId) {
        if (lojaRepository.buscarPorId(lojaBaseId).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Loja base não encontrada.");
        }
    }

    private void preencherDados(Motorista motorista, MotoristaRequestDTO dto, String cpf) {
        motorista.setNomeCompleto(dto.getNomeCompleto().trim());
        motorista.setCpf(cpf);
        motorista.setNumeroCnh(dto.getNumeroCnh());
        motorista.setLojaBaseId(dto.getLojaBaseId());
        motorista.setValorDiariaMotorista(dto.getValorDiariaMotorista());
    }

    private String somenteDigitos(String valor) {
        return valor == null ? null : valor.replaceAll("\\D", "");
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }

    private MotoristaResponseDTO converterParaDto(Motorista motorista) {
        MotoristaResponseDTO dto = new MotoristaResponseDTO();
        dto.setId(motorista.getId());
        dto.setNomeCompleto(motorista.getNomeCompleto());
        dto.setCpf(motorista.getCpf());
        dto.setNumeroCnh(motorista.getNumeroCnh());
        dto.setLojaBaseId(motorista.getLojaBaseId());
        dto.setValorDiariaMotorista(motorista.getValorDiariaMotorista());
        dto.setIsDisponivel(motorista.getIsDisponivel());
        return dto;
    }
}
