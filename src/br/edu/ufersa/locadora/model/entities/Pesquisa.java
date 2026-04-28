package br.edu.ufersa.locadora.model.entities;

public class Pesquisa {

    //pesquisa de livros e discos
    public List<ItemAcervo> buscarItem(String titulo, String criadoPor, String genero, String dataDeLancamento, Boolean isDisco) {
        List<ItemAcervo> resultado = new ArrayList<>();

        for (ItemAcervo item : ItemAcervo.getItensDoAcervo()) {
            //filtros de pesquisa

            if (isDisco != null && item.getIsDisco() != isDisco) {continue;} //filtro de tipo (livro ou disco)

            if (titulo != null && !titulo.isEmpty() &&
            !item.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {continue;} //filtro de titulo

            if (criadoPor != null && !criadoPor.isEmpty() &&
            !item.getCriadoPor().toLowerCase().contains(criadoPor.toLowerCase())) {continue;} //filtro de autoria

            if (genero != null && !genero.isEmpty() &&
            !item.getGenero().toLowerCase().contains(genero.toLowerCase())) {continue;} //filtro de genero

            if (dataDeLancamento != null && !dataDeLancamento.isEmpty() &&
            !item.getDataDeLancamento().equals(dataDeLancamento)) {continue;}//filtro de data de lançamento

            resultado.add(item);
        }
        return resultado;
    }

    //pesquisa de cliente
    public List<Cliente> buscarCliente(String nome, String endereco, String cpf) {
        List<Cliente> resultado = new ArrayList<>();

        for (Cliente c : Cliente.getClientes()) {
            //filtros de pesquisa
            if (nome != null && !nome.isEmpty() &&
            !c.getNome().toLowerCase().contains(nome.toLowerCase())) {continue;} //filtro de nome

        if (endereco != null && !endereco.isEmpty() &&
            !c.getEndereco().toLowerCase().contains(endereco.toLowerCase())) {continue;} //filtro de endereço

        if (cpf != null && !cpf.isEmpty() &&
            !c.getCpf().equals(cpf)) {continue;}//filtro de cpf

            resultado.add(c);
        }
        return resultado;
    }
}