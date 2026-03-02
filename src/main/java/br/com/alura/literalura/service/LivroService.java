package br.com.alura.literalura.service;

import br.com.alura.literalura.dto.LivroDTO;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    // Método auxiliar para normalizar texto
    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }

    @Transactional
    public Livro salvarLivro(LivroDTO livroDTO) {
        // Verificar se livro já existe (usando o método simples)
        List<Livro> livrosExistentes = livroRepository.findByTituloContainingIgnoreCase(livroDTO.titulo());

        if (!livrosExistentes.isEmpty()) {
            return livrosExistentes.get(0);
        }

        // Criar ou buscar autor
        Autor autor = null;
        if (livroDTO.autores() != null && !livroDTO.autores().isEmpty()) {
            var autorDTO = livroDTO.autores().get(0);

            Optional<Autor> autorExistente = autorRepository.findByNomeContainingIgnoreCase(autorDTO.nome());

            if (autorExistente.isPresent()) {
                autor = autorExistente.get();
            } else {
                autor = new Autor();
                autor.setNome(autorDTO.nome());
                autor.setAnoNascimento(autorDTO.anoNascimento());
                autor.setAnoFalecimento(autorDTO.anoFalecimento());
                autor = autorRepository.save(autor);
            }
        } else {
            autor = new Autor();
            autor.setNome("Desconhecido");
            autor = autorRepository.save(autor);
        }

        // Criar livro
        Livro livro = new Livro();
        livro.setTitulo(livroDTO.titulo());
        livro.setIdioma(livroDTO.idiomas() != null && !livroDTO.idiomas().isEmpty() ?
                livroDTO.idiomas().get(0) : "Desconhecido");
        livro.setNumeroDownloads(livroDTO.numeroDownloads());
        livro.setAutor(autor);

        autor.adicionarLivro(livro);

        return livroRepository.save(livro);
    }

    // MÉTODO CORRIGIDO - Agora usa buscarPorTermo
    public List<Livro> buscarLivrosNoBancoPorTermo(String termo) {
        return livroRepository.buscarPorTermo(termo.toLowerCase());
    }

    public List<Livro> listarTodosLivros() {
        return livroRepository.findAll();
    }

    public List<Autor> listarTodosAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresVivosEmAno(Integer ano) {
        return autorRepository.findAutoresVivosEmDeterminadoAno(ano);
    }

    public List<Livro> listarLivrosPorIdioma(String idioma) {
        return livroRepository.findByIdioma(idioma);
    }
}