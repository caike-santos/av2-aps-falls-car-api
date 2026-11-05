package falls_car_api.application.auth;

import falls_car_api.domain.cadastro.Cliente;
import falls_car_api.domain.cadastro.IClienteRepository;
import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.usuario.IUsuarioRepository;
import falls_car_api.domain.usuario.Role;
import falls_car_api.domain.usuario.Usuario;
import falls_car_api.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthApplicationService {

    private final IUsuarioRepository usuarioRepository;
    private final IClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthApplicationService(
            IUsuarioRepository usuarioRepository,
            IClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponseDTO registrar(RegistroRequestDTO dto) {
        if (usuarioRepository.buscarPorEmail(dto.getEmail()).isPresent()) {
            throw new DomainException("Já existe um usuário cadastrado com este e-mail.");
        }

        Role role = dto.getRole() != null ? dto.getRole() : Role.CLIENTE;
        UUID clienteId = dto.getClienteId();

        if (role == Role.CLIENTE && clienteId == null && dto.getCpf() != null) {
            Cliente novoCliente = new Cliente();
            UUID novoClienteId = UUID.randomUUID();
            novoCliente.setId(novoClienteId);
            novoCliente.setNomeCompleto(dto.getNome());
            novoCliente.setEmail(dto.getEmail());
            novoCliente.setCpf(dto.getCpf());
            novoCliente.setTelefone(dto.getTelefone());
            novoCliente.setNumeroCnh(dto.getNumeroCnh());

            clienteRepository.salvar(novoCliente);
            clienteId = novoClienteId;
        }

        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        Usuario novoUsuario = new Usuario(
                UUID.randomUUID(),
                dto.getNome(),
                dto.getEmail(),
                senhaCriptografada,
                role,
                clienteId
        );
        Usuario salvo = usuarioRepository.salvar(novoUsuario);

        String token = jwtService.gerarToken(salvo);
        return LoginResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .usuarioId(salvo.getId())
                .clienteId(salvo.getClienteId())
                .nome(salvo.getNome())
                .email(salvo.getEmail())
                .role(salvo.getRole())
                .build();
    }

    public LoginResponseDTO autenticar(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.buscarPorEmail(dto.getEmail())
                .orElseThrow(() -> new DomainException("E-mail ou senha inválidos."));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new DomainException("E-mail ou senha inválidos.");
        }

        String token = jwtService.gerarToken(usuario);
        return LoginResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .usuarioId(usuario.getId())
                .clienteId(usuario.getClienteId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .build();
    }
}
