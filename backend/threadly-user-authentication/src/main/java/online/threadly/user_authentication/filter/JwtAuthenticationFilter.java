package online.threadly.user_authentication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import online.threadly.user_authentication.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String jwtToken = authorizationHeader.substring(7);
      if (jwtToken != null) {
        jwtToken = jwtToken.trim();
        if (jwtToken.length() >= 2 && jwtToken.startsWith("\"") && jwtToken.endsWith("\"")) {
          jwtToken = jwtToken.substring(1, jwtToken.length() - 1);
        }
      }

      String userEmail = jwtService.extractUsername(jwtToken);

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (userEmail != null && authentication == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        if (jwtService.isTokenValid(jwtToken, userDetails)) {
          UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authenticationToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }

      filterChain.doFilter(request, response);
    } catch (Exception exception) {
      System.err.println("Invalid JWT Token." + exception.getMessage());
    }
  }
}

// Authorization: Bearer
// eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInN1YiI6InV0a2Fyc2guamFpbkBnbWFpbC5jb20iLCJpYXQiOjE3NTc2MDI0MTcsImV4cCI6MTc1NzY4ODgxN30.JmwhBaoa4HAdpIggcFJDNRA_WzhbtgBokhrFSwEUDokCUCFDN68nMGN2JQAGHtvKaSSYjdpmYqZvyi5sGxaLxg
