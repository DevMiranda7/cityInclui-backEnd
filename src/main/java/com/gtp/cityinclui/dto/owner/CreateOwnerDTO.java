    package com.gtp.cityinclui.dto.owner;

    import com.gtp.cityinclui.entity.Owner;

    import java.util.Objects;

    public class CreateOwnerDTO {

        private String nomeDoRestaurante;

        private String nomeDoAnunciante;

        private String cardapio;

        private String email;

        private String telefone;

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
