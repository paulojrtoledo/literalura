package br.com.alura.literalura.service;

import br.com.alura.literalura.dto.GutendexResponse;
import br.com.alura.literalura.dto.LivroDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Service
public class GutendexService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://gutendex.com/books?search=";

    // Método para remover acentos e normalizar texto
    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // Remove acentos
                .toLowerCase()
                .trim();
    }

    // Nova método que retorna LISTA de livros (para partial match)
    public List<LivroDTO> buscarLivrosPorTitulo(String termoOriginal) {
        List<LivroDTO> resultados = new ArrayList<>();

        try {
            // Normaliza o termo digitado pelo usuário
            String termoNormalizado = normalizarTexto(termoOriginal);

            // Codifica para URL
            String url = BASE_URL + URLEncoder.encode(termoNormalizado, StandardCharsets.UTF_8);

            // Faz a requisição à API
            GutendexResponse response = restTemplate.getForObject(url, GutendexResponse.class);

            if (response != null && response.resultados() != null) {
                // Filtra os resultados que contêm o termo normalizado
                for (LivroDTO livro : response.resultados()) {
                    String tituloNormalizado = normalizarTexto(livro.titulo());

                    // Verifica se o termo está contido no título (partial match)
                    if (tituloNormalizado.contains(termoNormalizado)) {
                        resultados.add(livro);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar livros na API: " + e.getMessage());
        }

        return resultados;
    }

    // Mantém o método original para compatibilidade (retorna apenas o primeiro)
    public LivroDTO buscarPrimeiroLivroPorTitulo(String tituloOriginal) {
        List<LivroDTO> resultados = buscarLivrosPorTitulo(tituloOriginal);
        return resultados.isEmpty() ? null : resultados.get(0);
    }
}