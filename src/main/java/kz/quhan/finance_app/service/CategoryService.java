package kz.quhan.finance_app.service;

import com.fasterxml.jackson.databind.JsonNode;
import kz.quhan.finance_app.entity.Category;
import kz.quhan.finance_app.exception.CategoryException;
import kz.quhan.finance_app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getList() {
        return categoryRepository.findAll();
    }

    public Category getOne(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException("categiry with this id doesn't exist"));
    }

    public List<Category> getMany(Collection<Integer> ids) {
        return categoryRepository.findAllById(ids);
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category patch(Integer id, Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    public List<Category> patchMany(Collection<Category> ids) {
        return categoryRepository.saveAll(ids);
    }

    public void delete(Category id) {
        categoryRepository.delete(id);
    }

    public void deleteMany(Collection<Integer> ids) {
        categoryRepository.deleteAllById(ids);
    }
}
