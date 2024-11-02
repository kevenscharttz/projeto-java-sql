package dataBase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import dataBase.Model.pokemonModel;

public class pokemonDAO {
    
    private String select = "SELECT * FROM tb_pokemon";
    private String insert = "INSERT INTO tb_pokemon(pokemon, tipo) values (?, ?)";
    
    private PreparedStatement pstSelect;
    private PreparedStatement pstInsert;
    
    public pokemonDAO(Connection conexao) throws SQLException {
        pstSelect = conexao.prepareStatement(select);
        pstInsert = conexao.prepareStatement(insert);
    }
    
    public void inserir(pokemonModel model) throws SQLException {
        // Insere apenas na tabela principal tb_pokemon
        pstInsert.clearParameters();
        pstInsert.setString(1, model.getPokemon());
        pstInsert.setString(2, model.getTipo());
        pstInsert.execute(); // Inserindo na tabela principal
    }
    
    public ArrayList<pokemonModel> selectAll() throws SQLException {
        ArrayList<pokemonModel> listaUsuarios = new ArrayList<>();
        ResultSet resultado = pstSelect.executeQuery();
        
        while (resultado.next()) {
            pokemonModel usuario = new pokemonModel(resultado.getString("pokemon"), resultado.getString("tipo"));
            usuario.setId(resultado.getLong("id")); // assumindo que há uma coluna ID
            listaUsuarios.add(usuario);                
        }
        return listaUsuarios;
    }
}
