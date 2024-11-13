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
    private String insertDeletado = "INSERT INTO tb_pokemon_deletado(id, pokemon, tipo) VALUES (?, ?, ?)"; 
    private String insertTotalizador = "INSERT INTO tb_pokemon_totalizador(tipo, quantidade, duplicados) VALUES (?, ?, ?)";
    private String delete = "DELETE FROM tb_pokemon WHERE id = ?";  
    private PreparedStatement pstSelect;
    private PreparedStatement pstInsert;
    private PreparedStatement pstInsertTipoEletrico;
    private PreparedStatement pstInsertTipoFogo;
    private PreparedStatement pstInsertTipoVoador;
    private PreparedStatement pstInsertDeletado;  
    private PreparedStatement pstInsertTotalizador;  
    private PreparedStatement pstDelete;  

    public pokemonDAO(Connection conexao) throws SQLException {
        pstSelect = conexao.prepareStatement(select);
        pstInsert = conexao.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
        pstInsertTipoEletrico = conexao.prepareStatement(insertEletrico);
        pstInsertTipoFogo = conexao.prepareStatement(insertFogo);
        pstInsertTipoVoador = conexao.prepareStatement(insertVoador);
        pstInsertDeletado = conexao.prepareStatement(insertDeletado);  
        pstInsertTotalizador = conexao.prepareStatement(insertTotalizador);  
        pstDelete = conexao.prepareStatement(delete); 
    }

    public void inserir(pokemonModel model) throws SQLException {
        if (verificarDuplicadoNaTabelaPrincipal(model)) {
            moverParaTabelaDeletado(model);
            return;  
        }
        pstInsert.clearParameters();
        pstInsert.setString(1, model.getPokemon());
        pstInsert.setString(2, model.getTipo());
        pstInsert.execute();

        ResultSet rs = pstInsert.getGeneratedKeys();
        if (rs.next()) {
            model.setId(rs.getInt(1));  
        }
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

    private boolean verificarDuplicadoNaTabelaPrincipal(pokemonModel pokemon) throws SQLException {
        String query = "SELECT id FROM tb_pokemon WHERE pokemon = ?";
        PreparedStatement pstCheck = pstSelect.getConnection().prepareStatement(query);
        pstCheck.setString(1, pokemon.getPokemon());
        ResultSet rs = pstCheck.executeQuery();
        
        if (rs.next()) {
        	pokemon.setId(rs.getInt(1));
            return rs.getInt(1) > 0;  
        }
        return false; 
    }

    private void moverParaTabelaDeletado(pokemonModel model) throws SQLException {
        pstInsertDeletado.clearParameters();
        pstInsertDeletado.setInt(1, model.getId());
        pstInsertDeletado.setString(2, model.getPokemon());
        pstInsertDeletado.setString(3, model.getTipo());
        pstInsertDeletado.execute();
    }

    public boolean verificarDuplicado(String tipo, String pokemon) throws SQLException {
        String query = "SELECT COUNT(*) FROM tb_pokemon_" + tipo.toLowerCase() + " WHERE pokemon = ?";
        PreparedStatement pstCheck = pstSelect.getConnection().prepareStatement(query);
        pstCheck.setString(1, pokemon);
        ResultSet rs = pstCheck.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;  
    }

    public ArrayList<pokemonModel> selectAll() throws SQLException {
        ArrayList<pokemonModel> listaUsuarios = new ArrayList<>();
        ResultSet resultado = pstSelect.executeQuery();
        while (resultado.next()) {
            pokemonModel usuario = new pokemonModel(resultado.getString("pokemon"), resultado.getString("tipo"));
            usuario.setId(resultado.getInt("id"));
            listaUsuarios.add(usuario);
        }

        // Agora chama o método para registrar os totais
        registrarTotais(listaUsuarios);

        return listaUsuarios;
    }

    private void registrarTotais(ArrayList<pokemonModel> listaPokemons) throws SQLException {
        // Conta as quantidades por tipo
        int qtdEletrico = 0, qtdFogo = 0, qtdVoador = 0, duplicados = 0;

        for (pokemonModel pokemon : listaPokemons) {
            if (pokemon.getTipo().equals("Elétrico")) {
                qtdEletrico++;
            } else if (pokemon.getTipo().equals("Fogo")) {
                qtdFogo++;
            } else if (pokemon.getTipo().equals("Voador")) {
                qtdVoador++;
            }

            // Verifica se o Pokémon é duplicado
            if (verificarDuplicadoNaTabelaPrincipal(pokemon)) {
                duplicados++;
            }
        }

        // Insere na tabela de totalizadores
        inserirTotalizador("Elétrico", qtdEletrico, duplicados);
        inserirTotalizador("Fogo", qtdFogo, duplicados);
        inserirTotalizador("Voador", qtdVoador, duplicados);
    }

    private void inserirTotalizador(String tipo, int quantidade, int duplicados) throws SQLException {
        pstInsertTotalizador.clearParameters();
        pstInsertTotalizador.setString(1, tipo);
        pstInsertTotalizador.setInt(2, quantidade);
        pstInsertTotalizador.setInt(3, duplicados);
        pstInsertTotalizador.execute();
    }
}
