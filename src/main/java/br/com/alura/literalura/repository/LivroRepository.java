package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    List<Livro> findByIdioma(String idioma);

    @Query("SELECT l FROM Livro l WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Livro> buscarPorTermo(@Param("termo") String termo);

    List<Livro> findAllByOrderByTituloAsc();

    List<Livro> findByAutorId(Long autorId);

    List<Livro> findByNumeroDownloadsGreaterThan(Integer downloads);
}