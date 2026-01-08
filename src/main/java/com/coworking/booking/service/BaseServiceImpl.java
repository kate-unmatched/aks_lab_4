package com.coworking.booking.service;

import com.coworking.booking.jms.ChangeEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional
public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final ChangeEventService changeEventService;

    protected abstract String getEntityName();
    protected abstract ID getEntityId(T entity);

    @Override
    public T getById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entity not found: " + id));
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public T create(T entity) {
        T saved = repository.save(entity);

        changeEventService.sendInsert(
                getEntityName(),
                (Object) saved,
                (Long) getEntityId(saved)
        );

        return saved;
    }

    @Override
    public T update(ID id, T entity) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Entity not found: " + id);
        }

        T updated = repository.save(entity);

        changeEventService.sendUpdate(
                getEntityName(),
                (Object) updated,
                (Long) getEntityId(updated)
        );

        return updated;
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);

        changeEventService.sendDelete(
                getEntityName(),
                (Long) id
        );
    }
}
