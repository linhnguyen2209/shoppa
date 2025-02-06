package edu.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import edu.poly.domain.Category;

public interface CategoryService {

	void deleteAll();

	void deleteAll(Iterable<? extends Category> entities);

	Category getReferenceById(Long id);

	void deleteAllById(Iterable<? extends Long> ids);

	void delete(Category entity);

	Category getById(Long id);

	void deleteById(Long id);

	long count();

	void deleteAllInBatch();

	void deleteAllByIdInBatch(Iterable<Long> ids);

	boolean existsById(Long id);

	void deleteAllInBatch(Iterable<Category> entities);

	Optional<Category> findById(Long id);

	List<Category> findAllById(Iterable<Long> ids);

	List<Category> findAll();

	<S extends Category> List<S> saveAllAndFlush(Iterable<S> entities);

	<S extends Category> S saveAndFlush(S entity);

	Page<Category> findAll(Pageable pageable);

	Page<Category> search(String keyword, Pageable pageable);

	void flush();

	List<Category> findAll(Sort sort);

	<S extends Category> List<S> saveAll(Iterable<S> entities);

	<S extends Category> S save(S entity);
}
