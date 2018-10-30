package com.github.pedrobacchini.domain;

import java.util.Objects;

public class Compra {

    private String produto;

    private double valorPago;

    public Compra() {}

    public Compra(String produto, double valorPago) {
        this.produto = produto;
        this.valorPago = valorPago;
    }

    public String getProduto() { return produto; }

    public void setProduto(String produto) { this.produto = produto; }

    public double getValorPago() { return valorPago; }

    public void setValorPago(double valorPago) { this.valorPago = valorPago; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return Double.compare(compra.valorPago, valorPago) == 0 &&
                Objects.equals(produto, compra.produto);
    }
}
