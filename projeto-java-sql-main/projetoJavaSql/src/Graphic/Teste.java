package Graphic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dataBase.ConnectionFactory;
import dataBase.Model.pokemonModel;
import dataBase.dao.pokemonDAO;

public class Teste {
    public static void main(String[] args) throws SQLException {
        Connection conexao = ConnectionFactory.getConnectiom("localhost", "3306", "pokemon", "root", "unesc");

        if (conexao != null) {
            System.out.println("Conectou com sucesso!");
            List<pokemonModel> listaPokemon = new ArrayList<>();
            listaPokemon.add(new pokemonModel("Pikachu", "Elétrico"));
            listaPokemon.add(new pokemonModel("Miraidon", "Elétrico"));
            listaPokemon.add(new pokemonModel("Charmander", "Fogo"));
            listaPokemon.add(new pokemonModel("Fuecoco", "Fogo"));
            listaPokemon.add(new pokemonModel("Miraidon", "Elétrico"));
            listaPokemon.add(new pokemonModel("Pidgeotto", "Voador"));
            listaPokemon.add(new pokemonModel("Butterfree", "Voador"));
            listaPokemon.add(new pokemonModel("Butterfree", "Voador"));
            listaPokemon.add(new pokemonModel("Fuecoco", "Fogo"));
 
            pokemonDAO usuarioDAO = new pokemonDAO(conexao);

            for (pokemonModel model : listaPokemon) {
                System.out.println("Inserindo o pokemon: " + model.getPokemon());
                usuarioDAO.inserir(model); 
            }

            System.out.println("\nLista de Pokémon:");
            List<pokemonModel> todosPokemons = usuarioDAO.selectAll();
            for (pokemonModel pokemon : todosPokemons) {
                System.out.println(pokemon);
            }
            conexao.close();
        }
    }
}
