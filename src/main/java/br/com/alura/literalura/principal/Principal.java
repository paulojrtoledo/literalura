package br.com.alura.literalura.principal;

import br.com.alura.literalura.dto.LivroDTO;
import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.GutendexService;
import br.com.alura.literalura.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal {
    @Autowired
    private GutendexService gutendexService;

    @Autowired
    private LivroService livroService;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private Scanner scanner = new Scanner(System.in);

    public void exibirMenu() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- LITERALURA - CATÁLOGO DE LIVROS ---");
            System.out.println("1 - Buscar livro pelo título");
            System.out.println("2 - Listar livros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos em um determinado ano");
            System.out.println("5 - Listar livros por idioma");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        buscarLivro();
                        break;
                    case 2:
                        listarLivros();
                        break;
                    case 3:
                        listarAutores();
                        break;
                    case 4:
                        listarAutoresVivosPorAno();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido!");
            }
        }
    }

    private void buscarLivro() {
        System.out.print("Digite o título do livro (ou parte dele, com/sem acentos): ");
        String termo = scanner.nextLine();

        List<LivroDTO> livrosEncontrados = gutendexService.buscarLivrosPorTitulo(termo);

        if (livrosEncontrados.isEmpty()) {
            System.out.println("Nenhum livro encontrado na API!");
            System.out.println("Dica: Tente com títulos clássicos (Machado de Assis, Jane Austen, etc.)");
            return;
        }

        System.out.println("\n--- LIVROS ENCONTRADOS NA API (" + livrosEncontrados.size() + ") ---");
        for (int i = 0; i < livrosEncontrados.size(); i++) {
            LivroDTO livro = livrosEncontrados.get(i);
            String autorNome = (livro.autores() != null && !livro.autores().isEmpty())
                    ? livro.autores().get(0).nome()
                    : "Autor desconhecido";
            String idiomas = (livro.idiomas() != null && !livro.idiomas().isEmpty())
                    ? livro.idiomas().get(0)
                    : "idioma desconhecido";

            System.out.println((i + 1) + " - " + livro.titulo() +
                    " (" + autorNome + ") - " + idiomas);
        }

        System.out.print("\nDigite o número do livro que deseja salvar (ou 0 para cancelar): ");
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            if (opcao > 0 && opcao <= livrosEncontrados.size()) {
                LivroDTO livroSelecionado = livrosEncontrados.get(opcao - 1);
                Livro livro = livroService.salvarLivro(livroSelecionado);
                System.out.println("\n--- LIVRO SALVO COM SUCESSO ---");
                exibirDetalhesLivro(livro);
            } else if (opcao != 0) {
                System.out.println("Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido!");
        }
    }

    private void listarLivros() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
            return;
        }

        System.out.println("\n--- LIVROS REGISTRADOS (" + livros.size() + ") ---");
        for (int i = 0; i < livros.size(); i++) {
            System.out.println((i + 1) + " - " + livros.get(i).getTitulo() +
                    " (" + livros.get(i).getAutor().getNome() + ")");
        }

        System.out.print("\nDigite o número do livro para ver detalhes (ou 0 para voltar): ");
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            if (opcao > 0 && opcao <= livros.size()) {
                exibirDetalhesLivro(livros.get(opcao - 1));
            }
        } catch (NumberFormatException e) {
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
            return;
        }

        System.out.println("\n--- AUTORES REGISTRADOS (" + autores.size() + ") ---");
        for (int i = 0; i < autores.size(); i++) {
            Autor autor = autores.get(i);
            System.out.println((i + 1) + " - " + autor.getNome());
            System.out.println("   Ano nascimento: " + autor.getAnoNascimento());
            System.out.println("   Ano falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Ainda vivo"));
            System.out.println("   Livros: " + autor.getLivros().size());
            for (Livro livro : autor.getLivros()) {
                System.out.println("     * " + livro.getTitulo());
            }
            System.out.println("   ---");
        }
    }

    private void listarAutoresVivosPorAno() {
        System.out.print("Digite o ano: ");
        try {
            int ano = Integer.parseInt(scanner.nextLine());
            List<Autor> autores = autorRepository.findAutoresVivosEmDeterminadoAno(ano);

            if (autores.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado neste ano.");
                return;
            }

            System.out.println("\n--- AUTORES VIVOS EM " + ano + " (" + autores.size() + ") ---");
            autores.forEach(autor -> {
                System.out.println(autor.getNome() + " (nascido em " + autor.getAnoNascimento() +
                        (autor.getAnoFalecimento() != null ? ", falecido em " + autor.getAnoFalecimento() : "") + ")");
                System.out.println("   Livros: " + autor.getLivros().size());
            });
        } catch (NumberFormatException e) {
            System.out.println("Digite um ano válido!");
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.println("Escolha o idioma:");
        System.out.println("pt - Português");
        System.out.println("en - Inglês");
        System.out.println("fr - Francês");
        System.out.println("es - Espanhol");
        System.out.print("Digite a sigla: ");

        String idioma = scanner.nextLine().toLowerCase();
        List<Livro> livros = livroRepository.findByIdioma(idioma);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado neste idioma.");
            return;
        }

        System.out.println("\n--- LIVROS EM " + idioma.toUpperCase() + " (" + livros.size() + ") ---");
        for (int i = 0; i < livros.size(); i++) {
            Livro livro = livros.get(i);
            System.out.println((i + 1) + " - " + livro.getTitulo() +
                    " (" + livro.getAutor().getNome() + ")");
        }

        System.out.print("\nDigite o número do livro para ver detalhes (ou 0 para voltar): ");
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            if (opcao > 0 && opcao <= livros.size()) {
                exibirDetalhesLivro(livros.get(opcao - 1));
            }
        } catch (NumberFormatException e) {
        }
    }

    private void exibirDetalhesLivro(Livro livro) {
        System.out.println("\n=== DETALHES DO LIVRO ===");
        System.out.println("Título: " + livro.getTitulo());
        System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Desconhecido"));
        System.out.println("Idioma: " + livro.getIdioma());
        System.out.println("Número de downloads: " + livro.getNumeroDownloads());
        if (livro.getAutor() != null) {
            System.out.println("Sobre o autor:");
            System.out.println("  - Nascimento: " + livro.getAutor().getAnoNascimento());
            System.out.println("  - Falecimento: " + (livro.getAutor().getAnoFalecimento() != null ? livro.getAutor().getAnoFalecimento() : "Ainda vivo"));
        }
        System.out.println("=========================\n");
    }
}