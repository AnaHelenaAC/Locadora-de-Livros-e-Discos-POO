package br.edu.ufersa.locadora.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Aluguel {

    // ATRIBUTOS
    private int id;
    private final Cliente cliente;
    private List<ItemAluguel> itensAlugados;
    private final LocalDate dataInicio;
    private final LocalDate dataFimPrevista;
    private final double valorBase;
    private double valorMulta;

    // Construtor privado de Builder
    private Aluguel(Builder builder) {
        this.id = builder.id;
        this.cliente = builder.cliente;
        this.itensAlugados = builder.itensAlugados;
        this.dataInicio = builder.dataInicio;
        this.dataFimPrevista = builder.dataFimPrevista;
        this.valorBase = builder.valorBase;
        this.valorMulta = builder.valorMulta;
    }

    public static Builder builder() {
        return new Builder();
    }

    // MÉTODOS

    private double calcularMultaAcumulada() {
        double totalMulta = 0.0;
        for (ItemAluguel item : itensAlugados) {
            totalMulta += item.calcularMultaItem(dataFimPrevista, dataInicio);
        }
        return totalMulta;
    }

    // Consulta o valor final atualizado
    public double calcularValorFinalAtual() {return valorBase + calcularMultaAcumulada();
    }

    // Finaliza TODOS os itens de uma vez só (caso todos sejam devolvidos juntos)
    public void finalizarAluguelCompleto(LocalDate dataDevolucao) {
        if (getStatus().equals("FINALIZADO")) {
            throw new IllegalStateException("Aluguel já está completamente finalizado.");
        }

        for (ItemAluguel item : itensAlugados) {
            if (item.getDataFim() == null) { // finaliza apenas os que ainda faltam
                item.finalizarItem(dataDevolucao, dataInicio);
            }
        }
        // Atualiza a multa gravada no objeto
        this.valorMulta = calcularMultaAcumulada();
    }

    // Permite finalizar apenas um item específico (Devolução parcial)
    public void finalizarItemEspecifico(ItemAluguel item, LocalDate dataDevolucao) {
        if (!itensAlugados.contains(item)) {
            throw new IllegalArgumentException("Este item não pertence a este aluguel.");
        }
        item.finalizarItem(dataDevolucao, dataInicio);
        this.valorMulta = calcularMultaAcumulada();
    }

    public void adicionarItens(List<ItemAluguel> itens) {
        if (itens != null) {
            this.itensAlugados.addAll(itens);
        }
    }

    // Valor efetivamente pago após a finalização
    public double getValorTotalPago() {
        return valorBase + valorMulta;
    }

    // GETTERS E SETTERS
    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemAluguel> getItensAlugados() {
        return new ArrayList<>(itensAlugados);
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFimPrevista() {
        return dataFimPrevista;
    }

    public double getValorBase() {
        return valorBase;
    }

    public double getValorMulta() {
        return valorMulta;
    }

    public String getStatus() {
        boolean todosDevolvidos = true;
        for (ItemAluguel item : itensAlugados) {
            if (item.getDataFim() == null) {
                todosDevolvidos = false;
                break;
            }
        }

        if (todosDevolvidos) {
            return "FINALIZADO";
        }
        if (LocalDate.now().isAfter(dataFimPrevista)) {
            return "ATRASADO";
        }
        return "ATIVO";
    }

    public void setId(int id) {
        this.id = id;
    }

    //BUILDER
    public static class Builder {
        private int id;
        private Cliente cliente;
        private List<ItemAluguel> itensAlugados = new ArrayList<>();
        private LocalDate dataInicio;
        private LocalDate dataFimPrevista;
        private double valorBase;
        private double valorMulta = 0.0;

        private Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder cliente(Cliente cliente) {
            this.cliente = cliente;
            return this;
        }

        public Builder itensAlugados(List<ItemAluguel> itensAlugados) {
            this.itensAlugados = (itensAlugados != null) ? new ArrayList<>(itensAlugados) : new ArrayList<>();
            return this;
        }

        public Builder dataInicio(LocalDate dataInicio) {
            this.dataInicio = dataInicio;
            return this;
        }

        public Builder dataFimPrevista(LocalDate dataFimPrevista) {
            this.dataFimPrevista = dataFimPrevista;
            return this;
        }

        public Builder valorBase(double valorBase) {
            this.valorBase = valorBase;
            return this;
        }

        public Builder valorMulta(double valorMulta) {
            this.valorMulta = valorMulta;
            return this;
        }

        // Conveniência: monta um aluguel novo a partir de um carrinho
        public Builder fromCarrinho(Carrinho carrinho, int diasAlugados) {
            if (carrinho == null || carrinho.getItensNoCarrinho().isEmpty()) {
                throw new IllegalArgumentException("Carrinho vazio.");
            }
            if (diasAlugados <= 0) {
                throw new IllegalArgumentException("Quantidade de dias inválida.");
            }

            this.cliente = carrinho.getCliente();
            this.dataInicio = LocalDate.now();
            this.dataFimPrevista = this.dataInicio.plusDays(diasAlugados);
            this.valorBase = carrinho.calcularValorTotal();
            this.valorMulta = 0.0;

            this.itensAlugados = new ArrayList<>();
            for (ItemCarrinho itemCarrinho : carrinho.getItensNoCarrinho()) {
                this.itensAlugados.add(
                        ItemAluguel.builder()
                                .fromItemAcervo(itemCarrinho.getItemAcervo(), itemCarrinho.getDiasAlugados())
                                .build());
            }
            return this;
        }

        public Aluguel build() {
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente inválido.");
            }
            if (dataInicio == null || dataFimPrevista == null) {
                throw new IllegalArgumentException("Datas do aluguel inválidas.");
            }
            return new Aluguel(this);
        }
    }
}