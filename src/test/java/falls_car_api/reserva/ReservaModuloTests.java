package falls_car_api.reserva;

import falls_car_api.application.reserva.ReservaApplicationService;
import falls_car_api.application.reserva.ReservaRequestDTO;
import falls_car_api.application.reserva.ReservaResponseDTO;
import falls_car_api.domain.frota.Automovel;
import falls_car_api.domain.frota.IAutomovelRepository;
import falls_car_api.domain.loja.ILojaRepository;
import falls_car_api.domain.loja.Loja;
import falls_car_api.domain.loja.VoCoordenada;
import falls_car_api.domain.reserva.IReservaRepository;
import falls_car_api.domain.reserva.SemFrotaDisponivelNaCidadeException;
import falls_car_api.domain.reserva.StatusReserva;
import falls_car_api.domain.shared.CanalReserva;
import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.PeriodoLocacao;
import falls_car_api.domain.shared.StatusAutomovel;
import falls_car_api.domain.shared.TipoLoja;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReservaModuloTests {

    @Autowired
    private ReservaApplicationService reservaService;

    @Autowired
    private ILojaRepository lojaRepository;

    @Autowired
    private IAutomovelRepository automovelRepository;

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private MockMvc mockMvc;

    private Loja lojaAeroportoGaleao;
    private Loja lojaCentroRioMaisProxima;
    private Loja lojaBarraRioMaisDistante;

    @BeforeEach
    void setUp() {
        // Loja de retirada: Aeroporto Galeão (Rio de Janeiro)
        lojaAeroportoGaleao = criarLoja(
                "Falls Car Galeão",
                TipoLoja.AEROPORTO,
                "Rio de Janeiro",
                "RJ",
                -22.8099,
                -43.2505
        );

        // Loja mais próxima na mesma cidade: Centro (~14 km do Galeão)
        lojaCentroRioMaisProxima = criarLoja(
                "Falls Car Centro",
                TipoLoja.CIDADE,
                "Rio de Janeiro",
                "RJ",
                -22.9068,
                -43.1729
        );

        // Loja mais distante na mesma cidade: Barra da Tijuca (~24 km do Galeão)
        lojaBarraRioMaisDistante = criarLoja(
                "Falls Car Barra",
                TipoLoja.CIDADE,
                "Rio de Janeiro",
                "RJ",
                -23.0003,
                -43.3658
        );
    }

    @Test
    void deveAlocarAutomovelLivreNaPropriaLojaDeRetirada() {
        Automovel carroNaLojaRetirada = criarAutomovel("ABC1D23", new BigDecimal("100.00"), lojaAeroportoGaleao.getId());

        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setClienteId(UUID.randomUUID());
        dto.setLojaRetiradaId(lojaAeroportoGaleao.getId());
        dto.setCanalReserva(CanalReserva.INTERNET);
        dto.setPeriodoDias(7);
        dto.setIncluiMotorista(false);
        dto.setDataRetirada(Instant.parse("2026-11-10T10:00:00Z"));

        ReservaResponseDTO response = reservaService.criar(dto);

        assertNotNull(response.getId());
        assertNotNull(response.getCodigoReserva());
        assertEquals(carroNaLojaRetirada.getId(), response.getAutomovelId());
        assertEquals(lojaAeroportoGaleao.getId(), response.getLojaOrigemAutomovelId());
        assertEquals(lojaAeroportoGaleao.getId(), response.getLojaRetiradaId());
        assertEquals(PeriodoLocacao.SETE_DIAS, response.getPeriodoLocacao());
        assertEquals(7, response.getPeriodoDias());
        assertEquals(new BigDecimal("700.00"), response.getValorTotalEstimado());
        assertEquals(StatusReserva.PENDENTE_PAGAMENTO, response.getStatusReserva());

        Automovel atualizado = automovelRepository.buscarPorId(carroNaLojaRetirada.getId()).orElseThrow();
        assertEquals(StatusAutomovel.RESERVADO, atualizado.getStatusAutomovel());
    }

    @Test
    void deveAlocarAutomovelDaLojaMaisProximaNaMesmaCidadeQuandoLojaRetiradaNaoTiverFrotaLivre() {
        // Nenhum carro livre no Galeão, um livre na Barra (mais distante) e um livre no Centro (mais próxima)
        criarAutomovel("BAR9A99", new BigDecimal("120.00"), lojaBarraRioMaisDistante.getId());
        Automovel carroLojaMaisProxima = criarAutomovel("CEN1A11", new BigDecimal("100.00"), lojaCentroRioMaisProxima.getId());

        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setClienteId(UUID.randomUUID());
        dto.setLojaRetiradaId(lojaAeroportoGaleao.getId());
        dto.setCanalReserva(CanalReserva.TELEFONE);
        dto.setPeriodoDias(15);
        dto.setIncluiMotorista(true);
        dto.setDataRetirada(Instant.parse("2026-11-10T10:00:00Z"));

        ReservaResponseDTO response = reservaService.criar(dto);

        assertEquals(carroLojaMaisProxima.getId(), response.getAutomovelId());
        assertEquals(lojaCentroRioMaisProxima.getId(), response.getLojaOrigemAutomovelId());
        assertEquals(lojaAeroportoGaleao.getId(), response.getLojaRetiradaId());
        // (100 diária carro + 150 diária motorista) * 15 dias = 3750.00
        assertEquals(new BigDecimal("3750.00"), response.getValorTotalEstimado());

        Automovel atualizado = automovelRepository.buscarPorId(carroLojaMaisProxima.getId()).orElseThrow();
        assertEquals(StatusAutomovel.RESERVADO, atualizado.getStatusAutomovel());
    }

    @Test
    void deveLancarSemFrotaDisponivelNaCidadeExceptionQuandoNaoHouverNenhumVeiculoLivre() {
        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setClienteId(UUID.randomUUID());
        dto.setLojaRetiradaId(lojaAeroportoGaleao.getId());
        dto.setCanalReserva(CanalReserva.LOJA);
        dto.setPeriodoDias(30);
        dto.setIncluiMotorista(false);
        dto.setDataRetirada(Instant.parse("2026-11-10T10:00:00Z"));

        assertThrows(SemFrotaDisponivelNaCidadeException.class, () -> reservaService.criar(dto));
    }

    @Test
    void deveRejeitarPeriodoDiferenteDe7Ou15Ou30DiasComMensagemPadronizada() {
        criarAutomovel("PER1A23", new BigDecimal("100.00"), lojaAeroportoGaleao.getId());

        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setClienteId(UUID.randomUUID());
        dto.setLojaRetiradaId(lojaAeroportoGaleao.getId());
        dto.setCanalReserva(CanalReserva.INTERNET);
        dto.setPeriodoDias(10);
        dto.setIncluiMotorista(false);
        dto.setDataRetirada(Instant.parse("2026-11-10T10:00:00Z"));

        DomainException ex = assertThrows(DomainException.class, () -> reservaService.criar(dto));
        assertEquals("O período de locação deve ser obrigatoriamente de 7, 15 ou 30 dias.", ex.getMessage());
    }

    @Test
    void deveCancelarReservaEDevolverStatusLivreAoAutomovelViaEndpointRest() throws Exception {
        Automovel carro = criarAutomovel("CAN1A23", new BigDecimal("100.00"), lojaAeroportoGaleao.getId());
        UUID clienteId = UUID.fromString("a1b2c3d4-e5f6-7890-1234-56789abcdef0");

        String payload = """
                {
                  "clienteId": "%s",
                  "lojaRetiradaId": "%s",
                  "canalReserva": "INTERNET",
                  "periodoDias": 15,
                  "incluiMotorista": true,
                  "dataRetirada": "2026-11-10T10:00:00Z"
                }
                """.formatted(clienteId, lojaAeroportoGaleao.getId());

        mockMvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.automovelId").value(carro.getId().toString()))
                .andExpect(jsonPath("$.statusReserva").value("PENDENTE_PAGAMENTO"));

        UUID reservaId = reservaRepository.listarTodos().get(0).getId();

        mockMvc.perform(patch("/api/v1/reservas/" + reservaId + "/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusReserva").value("CANCELADA"));

        Automovel carroAposCancelamento = automovelRepository.buscarPorId(carro.getId()).orElseThrow();
        assertEquals(StatusAutomovel.LIVRE, carroAposCancelamento.getStatusAutomovel());
    }

    private Loja criarLoja(String nome, TipoLoja tipo, String cidade, String estado, double lat, double lon) {
        Loja loja = new Loja();
        loja.setId(UUID.randomUUID());
        loja.setNome(nome);
        loja.setTipo(tipo);
        loja.setCidade(cidade);
        loja.setEstado(estado);
        loja.setEnderecoCompleto("Endereço " + nome);
        loja.setCoordenada(new VoCoordenada(lat, lon));
        loja.setIsAtivo(true);
        lojaRepository.salvar(loja);
        return loja;
    }

    private Automovel criarAutomovel(String placa, BigDecimal valorDiaria, UUID lojaId) {
        Automovel automovel = new Automovel();
        automovel.setId(UUID.randomUUID());
        automovel.setPlaca(placa);
        automovel.setModelo("Corolla");
        automovel.setMarca("Toyota");
        automovel.setAno(2025);
        automovel.setValorDiaria(valorDiaria);
        automovel.setStatusAutomovel(StatusAutomovel.LIVRE);
        automovel.setLojaAtualId(lojaId);
        automovelRepository.salvar(automovel);
        return automovel;
    }
}
