    package com.gtp.cityinclui.dto.owner;

    import com.gtp.cityinclui.entity.Owner;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Pattern;
    import jakarta.validation.constraints.Size;

    import java.util.Objects;

    public class CreateOwnerDTO {

        @NotBlank(message = "O nome do restaurante não pode estar em branco")
        @Size(min = 2, max = 100, message = "O nome do restaurante deve ter entre 2 e 100 caracteres")
        private String nomeDoRestaurante;

        @NotBlank(message = "O nome do anunciante não pode estar em branco")
        @Size(min = 2, max = 100, message = "O nome do anunciante deve ter entre 2 e 50 caracteres")
        private String nomeDoAnunciante;

        @NotBlank(message = "O cardapio deve ser informado")
        private String cardapio;

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O formato do email é inválido")
        private String email;

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "^[0-9]{10,11}$", message = "O telefone deve conter apenas números (10 ou 11 dígitos)")
        private String telefone;

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        private String senha;

        public CreateOwnerDTO() {
        }

        public CreateOwnerDTO(String nomeDoRestaurante, String nomeDoAnunciante, String cardapio, String email, String telefone, String senha) {
            this.nomeDoRestaurante = nomeDoRestaurante;
            this.nomeDoAnunciante = nomeDoAnunciante;
            this.cardapio = cardapio;
            this.email = email;
            this.telefone = telefone;
            this.senha = senha;
        }

        public static Owner toEntity(CreateOwnerDTO createOwnerDTO){
            Owner owner = new Owner();
            owner.setNomeDoRestaurante(createOwnerDTO.getNomeDoRestaurante());
            owner.setNomeDoAnunciante(createOwnerDTO.getNomeDoAnunciante());
            owner.setCardapio(createOwnerDTO.getCardapio());
            owner.setEmail(createOwnerDTO.getEmail());
            owner.setTelefone(createOwnerDTO.getTelefone());
            owner.setSenha(createOwnerDTO.getSenha());

            return owner ;
        }

        public String getNomeDoRestaurante() {
            return nomeDoRestaurante;
        }

        public void setNomeDoRestaurante(String nomeDoRestaurante) {
            this.nomeDoRestaurante = nomeDoRestaurante;
        }

        public String getNomeDoAnunciante() {
            return nomeDoAnunciante;
        }

        public void setNomeDoAnunciante(String nomeDoAnunciante) {
            this.nomeDoAnunciante = nomeDoAnunciante;
        }

        public String getCardapio() {
            return cardapio;
        }

        public void setCardapio(String cardapio) {
            this.cardapio = cardapio;
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

        @Override
        public String toString() {
            return "CreateOwnerDTO{" +
                    "nomeDoRestaurante='" + nomeDoRestaurante + '\'' +
                    ", nomeDoAnunciante='" + nomeDoAnunciante + '\'' +
                    ", cardapio='" + cardapio + '\'' +
                    ", email='" + email + '\'' +
                    ", telefone='" + telefone + '\'' +
                    ", senha='" + senha + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CreateOwnerDTO that = (CreateOwnerDTO) o;
            return Objects.equals(nomeDoRestaurante, that.nomeDoRestaurante) && Objects.equals(nomeDoAnunciante, that.nomeDoAnunciante) && Objects.equals(cardapio, that.cardapio) && Objects.equals(email, that.email) && Objects.equals(telefone, that.telefone) && Objects.equals(senha, that.senha);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nomeDoRestaurante, nomeDoAnunciante, cardapio, email, telefone, senha);
        }
    }
