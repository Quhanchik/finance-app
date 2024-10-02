package kz.quhan.finance_app.restController;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.quhan.finance_app.entity.Category;
import kz.quhan.finance_app.repository.CategoryRepository;
import kz.quhan.finance_app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getList( ) {
        return categoryService.getList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getOne(@PathVariable Integer id) {
        Category category = categoryService.getOne(id);
        return ResponseEntity.ok(category);
    }

//    @PostMapping
//    public Category create(@RequestBody Category category) {
//        return categoryService.create(category);
//    }
}
