package br.com.livraria.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.livraria.domain.Categoria;
import br.com.livraria.domain.Livro;
import br.com.livraria.repository.LivroRepository;

@RestController
@RequestMapping("livros")
public class LivroController {

	private LivroRepository livroRepository;

	public LivroController(LivroRepository livroRepository) {
		this.livroRepository = livroRepository;
	}

	@PostMapping("/adiciona")
	public ResponseEntity<Livro> adiciona(@Valid @RequestBody Livro livro) {
		livroRepository.save(livro);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").buildAndExpand(livro.getId())
				.toUri();

		return ResponseEntity.created(location).body(livro);
	}

	@GetMapping("/lista/todos")
	public ResponseEntity<List<Livro>> listaTodos() {
		return ResponseEntity.ok(livroRepository.findAll());
	}

	@GetMapping
	public ResponseEntity<?> listaComPaginacao(@PageableDefault(size = 10, page = 0) Pageable pageable) {
		return ResponseEntity.ok(livroRepository.findAll(pageable));
	}

	@GetMapping("/buscaPorId/{id}")
	public ResponseEntity<Livro> buscaPorId(@PathVariable("id") Integer id) {
		return livroRepository.findById(id).map(livro -> ResponseEntity.ok().body(livro))
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/atualiza/{id}")
	public ResponseEntity<Livro> atualiza(@RequestBody Livro livro, @PathVariable("id") Integer id) {
		return livroRepository.findById(id).map(l -> {

			l.setNome(livro.getNome());

			return ResponseEntity.ok(l);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/deleta/{id}")
	public ResponseEntity<?> remove(@PathVariable("id") Integer id) {
		return livroRepository.findById(id).map(livro -> {
			livroRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
