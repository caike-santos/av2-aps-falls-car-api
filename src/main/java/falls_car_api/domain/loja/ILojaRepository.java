package falls_car_api.domain.loja;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ILojaRepository {
    
    // Padrões de nomenclatura obrigatórios por camada (Método no Repository)[cite: 3]
    void salvar(Loja entidade); //[cite: 3]
    Optional<Loja> buscarPorId(UUID id); //[cite: 3]
    List<Loja> listarTodos(); //[cite: 3]
    void atualizar(Loja entidade); //[cite: 3]
    void removerPorId(UUID id); //[cite: 3]

    // Contrato Interno (Usado pelo Dev 4 - Módulo de Reservas)[cite: 5, 6]
    List<Loja> buscarLojasMesmaCidadeOrdenadasPorProximidade(UUID lojaRetiradaId); //[cite: 5, 6]
}