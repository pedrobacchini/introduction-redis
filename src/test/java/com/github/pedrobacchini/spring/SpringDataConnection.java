package com.github.pedrobacchini.spring;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

public class SpringDataConnection {

    @Test
    public void testConnection() {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-redis.xml");

//        JedisConnectionFactory connectionFactory = context.getBean(JedisConnectionFactory.class);
        JedisConnectionFactory connectionFactory = (JedisConnectionFactory) context.getBean("jedisConnectionFactory");
        Assert.assertNotNull(connectionFactory);
        Assert.assertNotNull(connectionFactory.getConnection());
    }
}
