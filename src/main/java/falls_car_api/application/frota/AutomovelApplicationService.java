package falls_car_api.application.frota;

import falls_car_api.domain.frota.Automovel;
import falls_car_api.domain.frota.IAutomovelRepository;
import falls_car_api.domain.loja.ILojaRepository;
import falls_car_api.domain.loja.Loja;
import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.EntidadeNaoEncontradaException;
import falls_car_api.domain.shared.StatusAutomovel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutomovelApplicationService {

    private final IAutomovelRepository automovelRepository;
    private final ILojaRepository lojaRepository;

    @Transactional
    public AutomovelResponseDTO criar(AutomovelRequestDTO requestDto) {
        if (requestDto.getLojaAtualId() == null) {
            throw new DomainException("A loja atual do automóvel (lojaAtualId) é obrigatória.");
        }
        Loja loja = buscarLojaAtivaOuFalhar(requestDto.getLojaAtualId());
        String placa = normalizarPlaca(requestDto.getPlaca());
        if (automovelRepository.existePorPlaca(placa)) {
            throw new DomainException("Já existe um automóvel cadastrado com a placa " + placa + ".");
        }

        Automovel automovel = new Automovel();
        automovel.setId(UUID.randomUUID());
        automovel.setStatusAutomovel(StatusAutomovel.LIVRE);
        automovel.setLojaAtualId(loja.getId());
        preencherDadosCadastrais(automovel, requestDto, placa);

        automovelRepository.salvar(automovel);
        return converterParaDto(automovel);
    }

    public AutomovelResponseDTO buscarPorId(UUID id) {
        return converterParaDto(buscarOuFalhar(id));
    }

    public List<AutomovelResponseDTO> listarTodos() {
        List<AutomovelResponseDTO> resposta = new ArrayList<>();
        for (Automovel automovel : automovelRepository.listarTodos()) {
            resposta.add(converterParaDto(automovel));
        }
        return resposta;
    }

    public List<AutomovelResponseDTO> listarPorFiltro(StatusAutomovel statusAutomovel, UUID lojaAtualId, String cidade) {
        Set<UUID> lojasIds = null;

        if (cidade != null && !cidade.isBlank()) {
            lojasIds = new HashSet<>();
            for (Loja loja : lojaRepository.listarTodos()) {
                if (loja.getCidade() != null && loja.getCidade().trim().equalsIgnoreCase(cidade.trim())) {
                    lojasIds.add(loja.getId());
                }
            }
        }
        if (lojaAtualId != null) {
            if (lojasIds == null) {
                lojasIds = new HashSet<>();
                lojasIds.add(lojaAtualId);
            } else {
                lojasIds.retainAll(Set.of(lojaAtualId));
            }
        }

        List<AutomovelResponseDTO> resposta = new ArrayList<>();
        for (Automovel automovel : automovelRepository.listarPorFiltro(statusAutomovel, lojasIds)) {
            resposta.add(converterParaDto(automovel));
        }
        return resposta;
    }

    @Transactional
    public void atualizar(UUID id, AutomovelRequestDTO requestDto) {
        Automovel automovel = buscarOuFalhar(id);
        String placa = normalizarPlaca(requestDto.getPlaca());
        if (!placa.equals(automovel.getPlaca()) && automovelRepository.existePorPlaca(placa)) {
            throw new DomainException("Já existe um automóvel cadastrado com a placa " + placa + ".");
        }
        preencherDadosCadastrais(automovel, requestDto, placa);
        automovelRepository.atualizar(automovel);
    }

    @Transactional
    public void remover(UUID id) {
        Automovel automovel = buscarOuFalhar(id);
        if (automovel.getStatusAutomovel() == StatusAutomovel.ALUGADO
                || automovel.getStatusAutomovel() == StatusAutomovel.RESERVADO) {
            throw new DomainException("Não é possível remover um automóvel " + automovel.getStatusAutomovel() + ".");
        }
        automovelRepository.removerPorId(id);
    }

    @Transactional
    public AutomovelResponseDTO alterarStatus(UUID id, AlterarStatusAutomovelDTO dto) {
        Automovel automovel = buscarOuFalhar(id);

        switch (dto.getStatusAutomovel()) {
            case RESERVADO:
                automovel.reservar();
                break;
            case ALUGADO:
                automovel.alugar();
                break;
            case MANUTENCAO:
                automovel.enviarParaManutencao();
                break;
            case LIVRE: {
                UUID lojaId = dto.getNovaLojaId() != null ? dto.getNovaLojaId() : automovel.getLojaAtualId();
                if (dto.getNovaLojaId() != null) {
                    buscarLojaAtivaOuFalhar(lojaId);
                }
                automovel.liberarVeiculo(lojaId);
                break;
            }
        }

        automovelRepository.atualizar(automovel);
        return converterParaDto(automovel);
    }

    @Transactional
    public AutomovelResponseDTO transferirLoja(UUID id, TransferirLojaDTO dto) {
        Automovel automovel = buscarOuFalhar(id);
        Loja lojaDestino = buscarLojaAtivaOuFalhar(dto.getNovaLojaId());

        Loja lojaAtual = lojaRepository.buscarPorId(automovel.getLojaAtualId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Loja atual do automóvel não encontrada."));
        if (lojaAtual.getCidade() == null || lojaDestino.getCidade() == null
                || !lojaAtual.getCidade().trim().equalsIgnoreCase(lojaDestino.getCidade().trim())) {
            throw new DomainException("A transferência só é permitida entre lojas da mesma cidade.");
        }

        automovel.transferirPara(lojaDestino.getId());
        automovelRepository.atualizar(automovel);
        return converterParaDto(automovel);
    }

    private Automovel buscarOuFalhar(UUID id) {
        return automovelRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Automóvel não encontrado."));
    }

    private Loja buscarLojaAtivaOuFalhar(UUID lojaId) {
        Loja loja = lojaRepository.buscarPorId(lojaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Loja não encontrada."));
        if (!Boolean.TRUE.equals(loja.getIsAtivo())) {
            throw new DomainException("A loja informada está inativa.");
        }
        return loja;
    }

    private String normalizarPlaca(String placa) {
        return placa.trim().replace("-", "").toUpperCase(Locale.ROOT);
    }

    private void preencherDadosCadastrais(Automovel automovel, AutomovelRequestDTO dto, String placaNormalizada) {
        automovel.setPlaca(placaNormalizada);
        automovel.setModelo(dto.getModelo());
        automovel.setMarca(dto.getMarca());
        automovel.setAno(dto.getAno());
        automovel.setValorDiaria(dto.getValorDiaria());
    }

    private AutomovelResponseDTO converterParaDto(Automovel automovel) {
        AutomovelResponseDTO dto = new AutomovelResponseDTO();
        dto.setId(automovel.getId());
        dto.setPlaca(automovel.getPlaca());
        dto.setModelo(automovel.getModelo());
        dto.setMarca(automovel.getMarca());
        dto.setAno(automovel.getAno());
        dto.setValorDiaria(automovel.getValorDiaria());
        dto.setStatusAutomovel(automovel.getStatusAutomovel());
        dto.setLojaAtualId(automovel.getLojaAtualId());
        return dto;
    }
}
