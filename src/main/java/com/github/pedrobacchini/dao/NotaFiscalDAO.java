package com.github.pedrobacchini.dao;

import com.github.pedrobacchini.Conexao;
import com.github.pedrobacchini.domain.NotaFiscal;
import com.github.pedrobacchini.domain.Pessoa;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;

public class NotaFiscalDAO {

    private static final String CAMPO_CODIGO = "codigo";
    private static final String CAMPO_VALOR = "valor";

    private Jedis jedis = Conexao.getConexao();

    public void salvar(Pessoa pessoa, NotaFiscal notaFiscal) {
        String chave = getChave(pessoa);
        String json = notaFiscalParaJSON(notaFiscal);
        jedis.sadd(chave, json);
    }

    public Set<NotaFiscal> obterNotas(Pessoa pessoa) throws ParseException {
        String chave = getChave(pessoa);

        Set<String> notasString = jedis.smembers(chave);

        return gerarNotasFicais(notasString);
    }

    public void remover(Pessoa pessoa, NotaFiscal notaFiscal) {
        String chave = getChave(pessoa);
        String json = notaFiscalParaJSON(notaFiscal);
        jedis.srem(chave, json);
    }

    public void salvarOrdenado(Pessoa pessoa, NotaFiscal notaFiscal) {
        String chave = getChave(pessoa);
        String json = notaFiscalParaJSON(notaFiscal);
        jedis.zadd(chave, notaFiscal.getValor(), json);
    }

    public Set<NotaFiscal> obterNotasOrdenadas(Pessoa pessoa, double minimo, double maximo) throws ParseException {
        String chave = getChave(pessoa);

        Set<String> notasString = jedis.zrangeByScore(chave, minimo, maximo);

        return gerarNotasFicais(notasString);
    }

    public void removerOrdenado(Pessoa pessoa, double minimo, double maximo) {
        String chave = getChave(pessoa);
        jedis.zremrangeByScore(chave, minimo, maximo);
    }

    public String getChave(Pessoa pessoa) { return getClass().getSimpleName()+ ":" + pessoa.getNome(); }

    private Set<NotaFiscal> gerarNotasFicais(Set<String> notasString) throws ParseException {
        Set<NotaFiscal> notasFiscais = new HashSet<>();
        for (String notaString : notasString) {
            NotaFiscal notaFiscal = jsonParaNotaFiscal(notaString);
            notasFiscais.add(notaFiscal);
        }
        return notasFiscais;
    }

    public String notaFiscalParaJSON(NotaFiscal notaFiscal) {
        if(notaFiscal != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CAMPO_CODIGO, notaFiscal.getCodigo());
            jsonObject.put(CAMPO_VALOR, notaFiscal.getValor());
            return jsonObject.toJSONString();
        }
        return null;
    }

    private NotaFiscal jsonParaNotaFiscal(String json) throws ParseException {
        if(json!=null){
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            NotaFiscal notaFiscal = new NotaFiscal();
            notaFiscal.setCodigo((String) jsonObject.get(CAMPO_CODIGO));
            notaFiscal.setValor((Double) jsonObject.get(CAMPO_VALOR));
            return notaFiscal;
        }
        return null;
    }
}
