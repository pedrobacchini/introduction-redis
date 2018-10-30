package com.github.pedrobacchini.serializador;

import com.github.pedrobacchini.Conexao;
import com.github.pedrobacchini.domain.Pessoa;
import redis.clients.jedis.Jedis;

/*
Exemplo utilizando array de bytes
 */
import java.io.*;

public class SerializadorRedis {

    private Jedis jedis = Conexao.getConexao();

    public String getChave(Pessoa pessoa) { return getClass().getSimpleName() + ":" + pessoa.getNome(); }

    public void setObjeto(String chave, Serializable objeto) throws IOException {
        byte[] bytes = objetoParaBytes(objeto);
        jedis.set(chave.getBytes(), bytes);
    }

    public Serializable getObjeto(String chave) throws IOException, ClassNotFoundException {
        byte[] bytes = jedis.get(chave.getBytes());
        return (Serializable) bytesParaObjeto(bytes);
    }

    private byte[] objetoParaBytes(Serializable objetoSerializable) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        byte[] result;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(objetoSerializable);
            result = byteArrayOutputStream.toByteArray();
        } finally {
            byteArrayOutputStream.close();
            objectOutputStream.close();
        }
        return result;
    }

    private Object bytesParaObjeto(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } finally {
            byteArrayInputStream.close();
            objectInputStream.close();
        }
    }
}
