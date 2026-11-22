package br.com.diariotreino.core.auth.domains.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Criação de conta pelo app. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastroInDTO {

    @NotBlank(message = "Informe seu nome.")
    @Size(min = 2, max = 120, message = "Informe seu nome.")
    private String nome;

    @NotBlank(message = "Informe o e-mail.")
    @Email(message = "Informe um e-mail válido.")
    private String email;

    @NotBlank(message = "Informe a senha.")
    @Size(min = 8, max = 72, message = "A senha precisa ter pelo menos 8 caracteres.")
    private String senha;
}
