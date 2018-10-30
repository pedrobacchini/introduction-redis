package com.github.pedrobacchini.domain;

import java.util.Objects;

public class NotaFiscal {

    private String codigo;
    private Double valor;

    public NotaFiscal() { }

    public NotaFiscal(String codigo, Double valor) {
        this.codigo = codigo;
        this.valor = valor;
    }

    public String getCodigo() { return codigo; }

    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Double getValor() { return valor; }

    public void setValor(Double valor) { this.valor = valor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotaFiscal that = (NotaFiscal) o;
        return Objects.equals(codigo, that.codigo) &&
                Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, valor);
    }
}
