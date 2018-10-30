package com.github.pedrobacchini.pubsub;

public class CRM extends Assinante {

    @Override
    public void onMessage(String canal, String mensagem) {
        if(canal.equals(PDV.NOME_CANAL)) {
            String[] componentes = mensagem.split(":");
            System.out.println("CRM recebeu notificação de venda para o cliente " + componentes[1]);
            System.out.println("\t CRM irá registrar estes dados agora.");
        }
    }
}
