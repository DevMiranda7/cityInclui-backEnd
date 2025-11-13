package com.gtp.cityinclui.dto.customer;

import com.gtp.cityinclui.entity.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateCustomerDTO {

    @NotBlank(message = "O nome do cliente não pode estar em branco")
    @Size(min = 3, max = 50, message = "O nome do Cliente deve ter entre 3 e 50 caracteres")
    private String nomeCompleto;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O formato do email é inválido")
    private String email;

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "O telefone deve conter apenas números (10 ou 11 dígitos)")
    private String telefone;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String senha;

    public CreateCustomerDTO() {
    }

    public CreateCustomerDTO(String nomeCompleto, String email, String telefone, String senha) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }

    public static Customer toEntity(CreateCustomerDTO clientDTO){
        Customer novoCliente = new Customer();

        novoCliente.setNomeCompleto(clientDTO.getNomeCompleto());
        novoCliente.setEmail(clientDTO.getEmail());
        novoCliente.setTelefone(clientDTO.getTelefone());
        novoCliente.setSenha(clientDTO.getSenha());

        return novoCliente;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
