package br.edu.ufersa.locadora.model.entities;

public class Aluguel {

    // Atributos
    private Cliente cliente;
    private ItemAcervo item;
    private java.util.Date dataInicio;
    private java.util.Date dataFim;

    // Construtor
    public Aluguel(Cliente cliente, ItemAcervo item, java.util.Date dataInicio) {
        setCliente(cliente);
        setItem(item);
        setDataInicio(dataInicio);
    }
       //metodos
    ///calcular valor final do aluguel
    public double calcularValorFinal() {
        if (dataFim == null) {
            throw new IllegalStateException("O aluguel precisa ser finalizado antes de calcular o valor final.");
        }

        long diferenca = dataFim.getTime() - dataInicio.getTime();
        long dias = Math.max(1, diferenca / (1000 * 60 * 60 * 24));
        return dias * item.getValor();
    }
    ///finalizar aluguel
    public void finalizarAluguel() {
        if (this.dataFim != null) {
            throw new IllegalStateException("Aluguel já foi finalizado.");
        }
        setDataFim(new java.util.Date());
    }

    // Getters
    public Cliente getCliente() {return cliente;}
    public ItemAcervo getItem() {return item;}
    public java.util.Date getDataInicio() {return dataInicio;}
    public java.util.Date getDataFim() {return dataFim;}

    // Setters
    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente nulo.");
        }
        this.cliente = cliente;
    }
    public void setItem(ItemAcervo item) {
        if (item == null) {
            throw new IllegalArgumentException("Item nulo.");
        }
        this.item = item;
    }
    public void setDataInicio(java.util.Date dataInicio) {
        if (dataInicio == null) {
            throw new IllegalArgumentException("data de início nula.");
        }
        this.dataInicio = dataInicio;
    }
    public void setDataFim(java.util.Date dataFim) {
        if (dataFim == null) {
            throw new IllegalArgumentException("data de fim nula.");
        }
        this.dataFim = dataFim;
    }
}