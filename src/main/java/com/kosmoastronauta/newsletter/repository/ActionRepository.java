package com.kosmoastronauta.newsletter.repository;

import com.kosmoastronauta.newsletter.domain.Action;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRepository extends CrudRepository<Action,Long>
{
    List<Action> getActionsByGroupIdEquals(long groupId);
}
