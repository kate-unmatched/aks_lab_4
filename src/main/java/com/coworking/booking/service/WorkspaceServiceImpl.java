package com.coworking.booking.service;

import com.coworking.booking.entity.Workspace;
import com.coworking.booking.jms.ChangeEventService;
import com.coworking.booking.repository.WorkspaceRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceServiceImpl extends BaseServiceImpl<Workspace, Long> implements WorkspaceService {

    public WorkspaceServiceImpl(WorkspaceRepository repository, ChangeEventService changeEventService) {
        super(repository, changeEventService);
    }

    @Override
    public Workspace update(Long id, Workspace updated) {
        Workspace existing = getById(id);

        existing.setName(updated.getName());
        existing.setType(updated.getType());
        existing.setCapacity(updated.getCapacity());
        existing.setAvailable(updated.getAvailable());

        Workspace saved = repository.save(existing);

        changeEventService.sendUpdate(
                getEntityName(),
                saved,
                saved.getId()
        );

        return existing;
    }

    @Override
    protected String getEntityName() {
        return "Workspace";
    }

    @Override
    protected Long getEntityId(Workspace entity) {
        return entity.getId();
    }

}
