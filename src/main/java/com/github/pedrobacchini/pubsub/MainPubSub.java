package com.github.pedrobacchini.pubsub;

import javax.swing.*;
import java.awt.*;

public class MainPubSub extends JFrame {

    private final PDV pdv = new PDV();

    private JTextField txtCliente;
    private JTextField txtProduto;
    private JTextField txtQuantidade;
    private JTextField txtValor;
    private JButton btnEnviar;

    public static void main(String args[]) {
        MainPubSub mainPubSub = new MainPubSub();
        mainPubSub.setVisible(true);
    }

    private MainPubSub() {
        setTitle("PDV");

        this.setSize(400, 200);
        setLayout(new GridLayout(5, 2));

        add((new JLabel("Cliente")));
        txtCliente = new JTextField();
        add(txtCliente);
        add(new JLabel("Produto"));
        txtProduto = new JTextField();
        add(txtProduto);
        add(new JLabel("Quantidade"));
        txtQuantidade = new JTextField();
        add(txtQuantidade);
        add(new JLabel("Valor"));
        txtValor = new JTextField();
        add(txtValor);

        btnEnviar = new JButton("Enviar");
        add(btnEnviar);

        btnEnviar.addActionListener(e ->
                pdv.registrarVenda(
                        txtProduto.getText(),
                        txtCliente.getText(),
                        Integer.parseInt(txtQuantidade.getText()),
                        Double.parseDouble(txtValor.getText())));

        JButton btnFechar = new JButton("Fechar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        btnFechar.addActionListener(e -> MainPubSub.this.setVisible(false));
        add(btnFechar);
        setLocationRelativeTo(null);

        startSubscribers();
    }

    private void startSubscribers() {
        Thread threadERP = new Thread() {
            private ERP erp = new ERP();

            public void run() {
                System.out.println("Iniciando ERP");
                erp.assinar(PDV.NOME_CANAL);
            }
        };
        threadERP.start();

        Thread threadCRM = new Thread() {
            private CRM crm = new CRM();

            public void run() {
                System.out.println("Iniciando CRM");
                crm.assinar(PDV.NOME_CANAL);
            }
        };
        threadCRM.start();
    }
}
