package com.github.pedrobacchini.dao;

import com.github.pedrobacchini.Conexao;
import com.github.pedrobacchini.domain.Compra;
import com.github.pedrobacchini.domain.Pessoa;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/*
Exemplo utilizando Lista de JSON
 */
public class CompraDAO {

    private static final String CAMPO_PRODUTO = "produto";
    private static final String CAMPO_VALOR_PRODUTO = "valorPago";

    private Jedis jedis = Conexao.getConexao();

    public void salvar(Pessoa pessoa, Compra compra) {
        String chave = getChave(pessoa);
        String valor = compraParaJSON(compra);
        jedis.rpush(chave, valor);
    }

    public void remover(Pessoa pessoa, long inicio, Compra compra){
        String chave = getChave(pessoa);
        String valor = compraParaJSON(compra);
        jedis.lrem(chave, inicio, valor);
    }

    public List<Compra> obter(Pessoa pessoa, long inicio, long itens) throws ParseException {
        List<String> compras_redis = jedis.lrange(getChave(pessoa), inicio, inicio + itens);

        List<Compra> compras = new ArrayList<>();
        for (String compra_string : compras_redis) {
            Compra compra = jsonParaCompra(compra_string);
            compras.add(compra);
        }
        return compras;
    }

    public String getChave(Pessoa pessoa) { return getClass().getSimpleName() + ":" + pessoa.getNome(); }

    private Compra jsonParaCompra(String json) throws ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
        Compra compra = new Compra();
        compra.setProduto((String) jsonObject.get(CAMPO_PRODUTO));
        compra.setValorPago((Double) jsonObject.get(CAMPO_VALOR_PRODUTO));
        return compra;
    }

    public String compraParaJSON(Compra compra) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CAMPO_PRODUTO, compra.getProduto());
        jsonObject.put(CAMPO_VALOR_PRODUTO, compra.getValorPago());
        return jsonObject.toJSONString();
    }
}
