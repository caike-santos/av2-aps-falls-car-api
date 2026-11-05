package falls_car_api.infrastructure.cadastro;

import falls_car_api.domain.cadastro.IMotoristaRepository;
import falls_car_api.domain.cadastro.Motorista;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MotoristaRepositoryImpl implements IMotoristaRepository {

    private final MotoristaJpaRepository jpaRepository;

    @Override
    public void salvar(Motorista entidade) {
        jpaRepository.save(converterParaEntity(entidade));
    }

    @Override
    public Optional<Motorista> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(this::converterParaDominio);
    }

    @Override
    public List<Motorista> listarTodos() {
        List<Motorista> motoristas = new ArrayList<>();
        for (MotoristaEntity entity : jpaRepository.findAll()) {
            motoristas.add(converterParaDominio(entity));
        }
        return motoristas;
    }

    @Override
    public void atualizar(Motorista entidade) {
        salvar(entidade);
    }

    @Override
    public void removerPorId(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<Motorista> buscarPorCpf(String cpf) {
        return jpaRepository.findByCpf(cpf).map(this::converterParaDominio);
    }

    @Override
    public List<Motorista> listarDisponiveisPorLojas(Collection<UUID> lojasBaseIds) {
        List<Motorista> motoristas = new ArrayList<>();
        for (MotoristaEntity entity : jpaRepository.findAll()) {
            boolean disponivel = Boolean.TRUE.equals(entity.getIsDisponivel());
            boolean lojaOk = lojasBaseIds == null || lojasBaseIds.contains(entity.getLojaBaseId());
            if (disponivel && lojaOk) {
                motoristas.add(converterParaDominio(entity));
            }
        }
        return motoristas;
    }

    private MotoristaEntity converterParaEntity(Motorista m) {
        MotoristaEntity e = new MotoristaEntity();
        e.setId(m.getId());
        e.setNomeCompleto(m.getNomeCompleto());
        e.setCpf(m.getCpf());
        e.setNumeroCnh(m.getNumeroCnh());
        e.setLojaBaseId(m.getLojaBaseId());
        e.setValorDiariaMotorista(m.getValorDiariaMotorista());
        e.setIsDisponivel(m.getIsDisponivel());
        return e;
    }

    private Motorista converterParaDominio(MotoristaEntity e) {
        Motorista m = new Motorista();
        m.setId(e.getId());
        m.setNomeCompleto(e.getNomeCompleto());
        m.setCpf(e.getCpf());
        m.setNumeroCnh(e.getNumeroCnh());
        m.setLojaBaseId(e.getLojaBaseId());
        m.setValorDiariaMotorista(e.getValorDiariaMotorista());
        m.setIsDisponivel(e.getIsDisponivel());
        return m;
    }
}
