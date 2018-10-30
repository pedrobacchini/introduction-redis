package com.github.pedrobacchini.dao;

import com.github.pedrobacchini.Conexao;
import com.github.pedrobacchini.domain.Pessoa;
import redis.clients.jedis.Jedis;

/*
Exemplo utilizando Hash
 */
import java.util.Map;

public class PessoaDAO {

    public static final String CAMPO_NOME = "nome";
    public static final String CAMPO_SOBRENOME = "sobrenome";

    private Jedis jedis = Conexao.getConexao();

    public String getChave(Pessoa pessoa) { return getClass().getSimpleName() + ":" + pessoa.getNome(); }

    public void salvar(Pessoa pessoa) {
        jedis.hset(getChave(pessoa), CAMPO_NOME, pessoa.getNome());
        jedis.hset(getChave(pessoa), CAMPO_SOBRENOME, pessoa.getSobrenome());
    }

    public void remover(Pessoa pessoa) {
        jedis.hdel(getChave(pessoa), CAMPO_NOME, CAMPO_SOBRENOME);
    }

    public Pessoa obter(String chave) {
        if(jedis.exists(chave)) {
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(jedis.hget(chave, CAMPO_NOME));
            pessoa.setSobrenome(jedis.hget(chave, CAMPO_SOBRENOME));
            return pessoa;
        }
        return null;
    }

    public Pessoa obterPerformatico(String chave) {
        if(jedis.exists(chave)) {
            Pessoa pessoa = new Pessoa();
            Map<String, String> dados = jedis.hgetAll(chave);
            pessoa.setNome(dados.get(CAMPO_NOME));
            pessoa.setSobrenome(dados.get(CAMPO_SOBRENOME));
            return pessoa;
        }
        return null;
    }
}
