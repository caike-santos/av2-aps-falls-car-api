package falls_car_api.application.cadastro;

import falls_car_api.domain.cadastro.Cliente;
import falls_car_api.domain.cadastro.IClienteRepository;
import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteApplicationService {

    private final IClienteRepository clienteRepository;

    @Transactional
    public ClienteResponseDTO criarCliente(ClienteRequestDTO requestDto) {
        String cpf = somenteDigitos(requestDto.getCpf());
        if (clienteRepository.buscarPorCpf(cpf).isPresent()) {
            throw new DomainException("Já existe um cliente cadastrado com este CPF.");
        }

        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        preencherDados(cliente, requestDto, cpf);

        clienteRepository.salvar(cliente);
        return converterParaDto(cliente);
    }

    public ClienteResponseDTO buscarClientePorId(UUID id) {
        return converterParaDto(buscarOuFalhar(id));
    }

    public List<ClienteResponseDTO> listarTodos() {
        List<ClienteResponseDTO> resposta = new ArrayList<>();
        for (Cliente cliente : clienteRepository.listarTodos()) {
            resposta.add(converterParaDto(cliente));
        }
        return resposta;
    }

    @Transactional
    public ClienteResponseDTO atualizar(UUID id, ClienteRequestDTO requestDto) {
        Cliente cliente = buscarOuFalhar(id);
        String cpf = somenteDigitos(requestDto.getCpf());

        clienteRepository.buscarPorCpf(cpf).ifPresent(outro -> {
            if (!outro.getId().equals(id)) {
                throw new DomainException("Já existe outro cliente cadastrado com este CPF.");
            }
        });

        preencherDados(cliente, requestDto, cpf);
        clienteRepository.atualizar(cliente);
        return converterParaDto(cliente);
    }

    @Transactional
    public void remover(UUID id) {
        buscarOuFalhar(id);
        clienteRepository.removerPorId(id);
    }

    private Cliente buscarOuFalhar(UUID id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado."));
    }

    private void preencherDados(Cliente cliente, ClienteRequestDTO dto, String cpf) {
        cliente.setNomeCompleto(dto.getNomeCompleto().trim());
        cliente.setCpf(cpf);
        cliente.setEmail(dto.getEmail().trim());
        cliente.setTelefone(dto.getTelefone());
        cliente.setNumeroCnh(dto.getNumeroCnh());
    }

    private String somenteDigitos(String valor) {
        return valor == null ? null : valor.replaceAll("\\D", "");
    }

    private ClienteResponseDTO converterParaDto(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNomeCompleto(cliente.getNomeCompleto());
        dto.setCpf(cliente.getCpf());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setNumeroCnh(cliente.getNumeroCnh());
        return dto;
    }
}
