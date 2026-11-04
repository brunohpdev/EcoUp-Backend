package br.com.ecoup.resource;

import br.com.ecoup.business.EcoUpBusiness;
import br.com.ecoup.model.Categoria;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaResource {
    private final EcoUpBusiness business;
    public CategoriaResource(EcoUpBusiness business) { this.business = business; }

    @GetMapping
    public List<Categoria> listar() { return business.listarCategorias(); }

    @GetMapping("/{id}")
    public Categoria buscar(@PathVariable int id) { return business.buscarCategoria(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria criar(@RequestBody Categoria categoria) { return business.criarCategoria(categoria); }

    @PutMapping("/{id}")
    public Categoria atualizar(@PathVariable int id, @RequestBody Categoria categoria) {
        return business.atualizarCategoria(id, categoria);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable int id) { business.excluirCategoria(id); }
}
