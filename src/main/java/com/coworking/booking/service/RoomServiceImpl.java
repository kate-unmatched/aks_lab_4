package com.coworking.booking.service;

import com.coworking.booking.entity.Room;
import com.coworking.booking.entity.Workspace;
import com.coworking.booking.jms.ChangeEventService;
import com.coworking.booking.repository.RoomRepository;
import com.coworking.booking.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomServiceImpl extends BaseServiceImpl<Room, Long> implements RoomService {

    private final WorkspaceRepository workspaceRepository;

    public RoomServiceImpl(RoomRepository roomRepository, WorkspaceRepository workspaceRepository,
                           ChangeEventService changeEventService) {
        super(roomRepository, changeEventService);
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    protected String getEntityName() {
        return "Room";
    }

    @Override
    protected Long getEntityId(Room entity) {
        return entity.getId();
    }

    // ====== API methods ======

    @Override
    public List<Room> getByWorkspace(Long workspaceId) {
        return ((RoomRepository) repository).findByWorkspaceId(workspaceId);
    }

    @Override
    public Room createRoom(Long workspaceId, Room room) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Workspace not found: " + workspaceId)
                );

        room.setWorkspace(workspace);

        Room saved = repository.save(room);

        changeEventService.sendInsert(
                getEntityName(),
                saved,
                saved.getId()
        );

        return saved;
    }

    @Override
    public Room updateRoom(Long workspaceId, Long roomId, Room updated) {
        Room room = repository.findById(roomId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Room not found: " + roomId)
                );

        if (!room.getWorkspace().getId().equals(workspaceId)) {
            throw new IllegalArgumentException("Workspace mismatch");
        }

        room.setRoomName(updated.getRoomName());
        room.setSeats(updated.getSeats());
        room.setAvailable(updated.getAvailable());

        Room saved = repository.save(room);

        changeEventService.sendUpdate(
                getEntityName(),
                saved,
                saved.getId()
        );

        return saved;
    }

    @Override
    public void deleteRoom(Long workspaceId, Long roomId) {
        Room room = repository.findById(roomId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Room not found: " + roomId)
                );

        if (!room.getWorkspace().getId().equals(workspaceId)) {
            throw new IllegalArgumentException("Workspace mismatch");
        }

        repository.deleteById(roomId);

        changeEventService.sendDelete(
                getEntityName(),
                roomId
        );
    }
}
