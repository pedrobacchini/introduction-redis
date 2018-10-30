package com.github.pedrobacchini.spring;

import com.github.pedrobacchini.domain.Compra;
import com.github.pedrobacchini.domain.Pessoa;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CompraDAOTest {

    private ApplicationContext context;
    private Jedis jedis;
    private CompraDAO compraDAO;
    private Pessoa pessoa = new Pessoa("Pedro", "Bacchini");

    @Before
    public void before() {
        context = new ClassPathXmlApplicationContext("spring-redis.xml");
        compraDAO = context.getBean(CompraDAO.class);
        jedis = ((JedisConnectionFactory) context.getBean("jedisConnectionFactory")).getConnection().getNativeConnection();
        context.getBean(StringRedisTemplate.class).delete(compraDAO.getChave(pessoa));
    }

    @Test
    public void testPersistirCompras() {
        List<Compra> compras = new ArrayList<>();
        compras.add(new Compra("Computador", 800));
        compras.add(new Compra("Tablet", 800));
        compras.add(new Compra("Doces", 1200));

        for (Compra compra: compras) {
            compraDAO.salvarCompra(pessoa, compra);
        }
        String chave = compraDAO.getChave(pessoa);
        assertTrue("Não foi persistido", jedis.exists(chave));

        assertEquals(3, jedis.lrange(chave, 0, 100).size());

        List<Compra> comprasPersistidas = compraDAO.obterCompras(pessoa, 0, 100);
        assertNotNull(comprasPersistidas);
        assertFalse(comprasPersistidas.isEmpty());
        for (Compra compra: compras) {
            assertTrue(comprasPersistidas.contains(compra));
        }
    }
}