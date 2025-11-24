    package com.gtp.cityinclui.dto.owner;

    import com.gtp.cityinclui.entity.RestaurantOwner;
    import jakarta.validation.constraints.*;

    import java.util.List;
    import java.util.Objects;

    public class CreateRestaurantOwnerDTO {

        @NotBlank(message = "O nome do restaurante não pode estar em branco")
        @Size(min = 2, max = 100, message = "O nome do restaurante deve ter entre 2 e 100 caracteres")
        private String nomeDoRestaurante;

        @NotBlank(message = "O nome do anunciante não pode estar em branco")
        @Size(min = 2, max = 100, message = "O nome do anunciante deve ter entre 2 e 50 caracteres")
        private String nomeDoAnunciante;

        @NotBlank(message = "O cardapio deve ser informado")
        private String cardapio;

        @NotBlank(message = "Descrição do restaurante é obrigatória, isso melhora o nosso recurso de acessibilidade")
        @Size(min = 100, max = 500, message = "A descrição do restaurante deverá ter entre 100 e máximo 500 caracteres")
        private String descricao;

        @NotEmpty(message = "É obrigatório cadastrar pelo menos uma acessibilidade")
        private List<String> acessibilidades;

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O formato do email é inválido")
        private String email;

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "^[0-9]{10,11}$", message = "O telefone deve conter apenas números (10 ou 11 dígitos)")
        private String telefone;

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        private String senha;

        public CreateRestaurantOwnerDTO() {
        }

        public CreateRestaurantOwnerDTO(String nomeDoRestaurante, String nomeDoAnunciante, String cardapio, String descricao, List<String> acessibilidades, String email, String telefone, String senha) {
            this.nomeDoRestaurante = nomeDoRestaurante;
            this.nomeDoAnunciante = nomeDoAnunciante;
            this.cardapio = cardapio;
            this.descricao = descricao;
            this.acessibilidades = acessibilidades;
            this.email = email;
            this.telefone = telefone;
            this.senha = senha;
        }

        public static RestaurantOwner toEntity(CreateRestaurantOwnerDTO createRestaurantOwnerDTO){
            RestaurantOwner restaurantOwner = new RestaurantOwner();
            restaurantOwner.setNomeDoRestaurante(createRestaurantOwnerDTO.getNomeDoRestaurante());
            restaurantOwner.setNomeDoAnunciante(createRestaurantOwnerDTO.getNomeDoAnunciante());
            restaurantOwner.setCardapio(createRestaurantOwnerDTO.getCardapio());
            restaurantOwner.setDescricao(createRestaurantOwnerDTO.getDescricao());
            restaurantOwner.setEmail(createRestaurantOwnerDTO.getEmail());
            restaurantOwner.setTelefone(createRestaurantOwnerDTO.getTelefone());
            restaurantOwner.setSenha(createRestaurantOwnerDTO.getSenha());

            return restaurantOwner;
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

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public List<String> getAcessibilidades() {
            return acessibilidades;
        }

        public void setAcessibilidades(List<String> acessibilidades) {
            this.acessibilidades = acessibilidades;
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
            CreateRestaurantOwnerDTO that = (CreateRestaurantOwnerDTO) o;
            return Objects.equals(nomeDoRestaurante, that.nomeDoRestaurante) && Objects.equals(nomeDoAnunciante, that.nomeDoAnunciante) && Objects.equals(cardapio, that.cardapio) && Objects.equals(email, that.email) && Objects.equals(telefone, that.telefone) && Objects.equals(senha, that.senha);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nomeDoRestaurante, nomeDoAnunciante, cardapio, email, telefone, senha);
        }
    }
