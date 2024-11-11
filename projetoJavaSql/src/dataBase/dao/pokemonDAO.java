 package dataBase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 
import dataBase.Model.pokemonModel;

public class pokemonDAO {

    private String select = "SELECT * FROM tb_pokemon";
    private String insert = "INSERT INTO tb_pokemon(pokemon, tipo) VALUES (?, ?)";
    private String insertEletrico = "INSERT INTO tb_pokemon_eletrico(id, pokemon, tipo) VALUES (?, ?, ?)";
    private String insertFogo = "INSERT INTO tb_pokemon_fogo(id, pokemon, tipo) VALUES (?, ?, ?)";
    private String insertVoador = "INSERT INTO tb_pokemon_voador(id, pokemon, tipo) VALUES (?, ?, ?)";

    private PreparedStatement pstSelect;
    private PreparedStatement pstInsert;
    private PreparedStatement pstInsertTipoEletrico;
    private PreparedStatement pstInsertTipoFogo;
    private PreparedStatement pstInsertTipoVoador;

    public pokemonDAO(Connection conexao) throws SQLException {
        pstSelect = conexao.prepareStatement(select);
        pstInsert = conexao.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS); 
        pstInsertTipoEletrico = conexao.prepareStatement(insertEletrico);
        pstInsertTipoFogo = conexao.prepareStatement(insertFogo);
        pstInsertTipoVoador = conexao.prepareStatement(insertVoador);
    }

    public void inserir(pokemonModel model) throws SQLException {
        // Insere na tabela principal tb_pokemon
        pstInsert.clearParameters();
        pstInsert.setString(1, model.getPokemon());
        pstInsert.setString(2, model.getTipo());
        pstInsert.execute(); // Inserindo na tabela principal

        // Recupera o ID gerado para o Pokémon
        ResultSet rs = pstInsert.getGeneratedKeys();
        if (rs.next()) {
            model.setId(rs.getInt(1));  // Captura o ID gerado e armazena no objeto PokemonModel
        }

        // Verifica e insere nas tabelas específicas de tipo (se não for duplicado)
        if (model.getTipo().equals("Elétrico") && !verificarDuplicado("eletrico", model.getPokemon())) {
            pstInsertTipoEletrico.clearParameters();
            pstInsertTipoEletrico.setInt(1, model.getId());
            pstInsertTipoEletrico.setString(2, model.getPokemon());
            pstInsertTipoEletrico.setString(3, model.getTipo());
            pstInsertTipoEletrico.execute();
        } else if (model.getTipo().equals("Fogo") && !verificarDuplicado("fogo", model.getPokemon())) {
            pstInsertTipoFogo.clearParameters();
            pstInsertTipoFogo.setInt(1, model.getId());
            pstInsertTipoFogo.setString(2, model.getPokemon());
            pstInsertTipoFogo.setString(3, model.getTipo());
            pstInsertTipoFogo.execute();
        } else if (model.getTipo().equals("Voador") && !verificarDuplicado("voador", model.getPokemon())) {
            pstInsertTipoVoador.clearParameters();
            pstInsertTipoVoador.setInt(1, model.getId());
            pstInsertTipoVoador.setString(2, model.getPokemon());
            pstInsertTipoVoador.setString(3, model.getTipo());
            pstInsertTipoVoador.execute();
        }
    }

    public boolean verificarDuplicado(String tipo, String pokemon) throws SQLException {
        // Agora está verificando a tabela de tipo correto
        String query = "SELECT COUNT(*) FROM tb_pokemon_" + tipo.toLowerCase() + " WHERE pokemon = ?";
        PreparedStatement pstCheck = pstSelect.getConnection().prepareStatement(query);
        pstCheck.setString(1, pokemon);
        ResultSet rs = pstCheck.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0;  // Retorna true se o Pokémon já existir na tabela do tipo
        }
        
        return false;  // Caso o Pokémon não exista, retorna false
    }

    public ArrayList<pokemonModel> selectAll() throws SQLException {
        ArrayList<pokemonModel> listaUsuarios = new ArrayList<>();
        ResultSet resultado = pstSelect.executeQuery();

        while (resultado.next()) {
            pokemonModel usuario = new pokemonModel(resultado.getString("pokemon"), resultado.getString("tipo"));
            usuario.setId(resultado.getInt("id")); 
            listaUsuarios.add(usuario);                
        }
        return listaUsuarios;
    }
}