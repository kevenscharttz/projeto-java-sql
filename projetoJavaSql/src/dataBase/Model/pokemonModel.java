package dataBase.Model;

public class pokemonModel {

    private int id;
    private String pokemon;
    private String tipo;

    public pokemonModel(String pokemon, String tipo) {
        this.pokemon = pokemon;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPokemon() {
        return pokemon;
    }

    public void setPokemon(String pokemon) {
        this.pokemon = pokemon;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
