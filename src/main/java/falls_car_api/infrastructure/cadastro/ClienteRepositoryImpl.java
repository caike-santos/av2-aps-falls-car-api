package falls_car_api.infrastructure.cadastro;

import falls_car_api.domain.cadastro.Cliente;
import falls_car_api.domain.cadastro.IClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryImpl implements IClienteRepository {

    private final ClienteJpaRepository jpaRepository;

    @Override
    public void salvar(Cliente entidade) {
        jpaRepository.save(converterParaEntity(entidade));
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(this::converterParaDominio);
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        for (ClienteEntity entity : jpaRepository.findAll()) {
            clientes.add(converterParaDominio(entity));
        }
        return clientes;
    }

    @Override
    public void atualizar(Cliente entidade) {
        salvar(entidade);
    }

    @Override
    public void removerPorId(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return jpaRepository.findByCpf(cpf).map(this::converterParaDominio);
    }

    private ClienteEntity converterParaEntity(Cliente c) {
        ClienteEntity e = new ClienteEntity();
        e.setId(c.getId());
        e.setNomeCompleto(c.getNomeCompleto());
        e.setCpf(c.getCpf());
        e.setEmail(c.getEmail());
        e.setTelefone(c.getTelefone());
        e.setNumeroCnh(c.getNumeroCnh());
        return e;
    }

    private Cliente converterParaDominio(ClienteEntity e) {
        Cliente c = new Cliente();
        c.setId(e.getId());
        c.setNomeCompleto(e.getNomeCompleto());
        c.setCpf(e.getCpf());
        c.setEmail(e.getEmail());
        c.setTelefone(e.getTelefone());
        c.setNumeroCnh(e.getNumeroCnh());
        return c;
    }
}
