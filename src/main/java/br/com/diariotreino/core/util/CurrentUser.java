package br.com.diariotreino.core.util;

import br.com.diariotreino.core.auth.security.UsuarioPrincipal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/** Acesso ao usuário autenticado (vem do JWT). */
public final class CurrentUser {

    private CurrentUser() {
    }

    public static UsuarioPrincipal get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UsuarioPrincipal p)) {
            throw new IllegalStateException("Nenhum usuário autenticado");
        }
        return p;
    }

    public static Long usuarioId() {
        return get().getId();
    }
}
