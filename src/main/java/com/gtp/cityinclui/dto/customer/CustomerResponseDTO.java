package com.gtp.cityinclui.dto.customer;

import com.gtp.cityinclui.entity.Customer;

public class CustomerResponseDTO {

    private Long id;

    private String nomeCompleto;

    private String email;

    private String telefone;

    public CustomerResponseDTO() {
    }

    public CustomerResponseDTO(Long id, String nomeCompleto, String email, String telefone) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.telefone = telefone;
    }

    public static CustomerResponseDTO fromEntity(Customer customer){
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setId(customer.getId());
        customerResponseDTO.setNomeCompleto(customer.getNomeCompleto());
        customerResponseDTO.setEmail(customer.getEmail());
        customerResponseDTO.setTelefone(customer.getTelefone());

        return customerResponseDTO;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
