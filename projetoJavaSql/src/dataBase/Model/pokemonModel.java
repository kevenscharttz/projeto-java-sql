package dataBase.Model;

public class pokemonModel {
	
	private long id;
	private String pokemon;
	private String tipo;
	

	public pokemonModel(String pokemon, String tipo) {
        this.pokemon = pokemon;
        this.tipo = tipo;
    }
	
		public long getId() {
			return id;
		}
		public void setId(long id) {
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
		
		public void pokemonEletricoModel(String pokemon, String tipo) {
	        this.pokemon = pokemon;
	        this.tipo = tipo;
	    }
		
		public void pokemonFogoModel(String pokemon, String tipo) {
	        this.pokemon = pokemon;
	        this.tipo = tipo;
	    }
		
		public void pokemonVoadorModel(String pokemon, String tipo) {
	        this.pokemon = pokemon;
	        this.tipo = tipo;
	    }
		
		
		
}
