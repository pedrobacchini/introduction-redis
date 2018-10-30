package com.github.pedrobacchini.pubsub;

public class ERP extends Assinante {

    @Override
    public void onMessage(String canal, String mensagem) {
        if(canal.equals(PDV.NOME_CANAL)) {
            String[] componentes = mensagem.split(":");
            System.out.println("ERP recebeu a venda do produto " + componentes[0]);
            System.out.println("\t ERP ira abater quantidade " + componentes[2] + " do estoque");
        }
    }
}
