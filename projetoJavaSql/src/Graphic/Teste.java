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

        // Conectando com o banco de dados
        Connection conexao = ConnectionFactory.getConnectiom("localhost", "3306", "pokemon", "root", "123123");

        if (conexao != null) {
            System.out.println("Conectou com sucesso!");

            // Lista dos Pokémon com base na sua lista fornecida
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

            // Criando instância do DAO
            pokemonDAO usuarioDAO = new pokemonDAO(conexao);

            // Inserindo todos os Pokémon na tabela principal e nas tabelas específicas
            for (pokemonModel model : listaPokemon) {
                System.out.println("Inserindo o pokemon: " + model.getPokemon());
                usuarioDAO.inserir(model);  // Inserção na tabela principal e nas tabelas de tipo
                System.out.println("Pokemon " + model.getPokemon() + " cadastrado com sucesso");
            }

            // Selecionando e imprimindo todos os Pokémon das tabelas de tipo
            ArrayList<pokemonModel> lista = usuarioDAO.selectAll();
            for (pokemonModel u : lista) {
                System.out.println(u.getPokemon() + " -> " + u.getTipo());
            }
        } else {
            System.out.println("Deu ruim rapazes!");
        }
    }
}