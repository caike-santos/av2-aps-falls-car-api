package falls_car_api.infrastructure.security;

import falls_car_api.domain.usuario.IUsuarioRepository;
import falls_car_api.domain.usuario.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final IUsuarioRepository usuarioRepository;
    public SecurityFilter(JwtService jwtService, IUsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extrairToken(request);
        if (token != null) {
            String email = jwtService.validarToken(token);
            if (email != null) {
                Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorEmail(email);
                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
    private String extrairToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}
