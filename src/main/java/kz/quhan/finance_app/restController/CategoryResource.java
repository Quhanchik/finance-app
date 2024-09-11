package kz.quhan.finance_app.restController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.quhan.finance_app.entity.Category;
import kz.quhan.finance_app.repository.CategoryRepository;
import kz.quhan.finance_app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryResource {

    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public List<Category> getList( ) {
        return categoryService.getList();
    }

    @GetMapping("/{id}")
    public Category getOne(@PathVariable Integer id) {
        Optional<Category> categoryOptional = categoryService.getOne(id);
        return categoryOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Category> getMany(@RequestParam List<Integer> ids) {
        return categoryService.getMany(ids);
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @PatchMapping("/{id}")
    public Category patch(@PathVariable Integer id, @RequestBody Category categoryPayload) throws IOException {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        return categoryService.patch(id, categoryPayload);
    }

    @PatchMapping
    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Category> categories = categoryRepository.findAllById(ids);

        for (Category category : categories) {
            objectMapper.readerForUpdating(category).readValue(patchNode);
        }

        List<Category> resultCategories = categoryService.patchMany(categories);
        List<Integer> ids1 = resultCategories.stream()
                .map(Category::getId)
                .toList();
        return ids1;
    }

    @DeleteMapping("/{id}")
    public Category delete(@PathVariable Integer id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            categoryService.delete(category);
        }
        return category;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        categoryService.deleteMany(ids);
    }
}
