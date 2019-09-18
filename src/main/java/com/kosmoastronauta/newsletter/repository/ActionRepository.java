package com.kosmoastronauta.newsletter.repository;

import com.kosmoastronauta.newsletter.domain.GroupAction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRepository extends CrudRepository<GroupAction,Long>
{
    List<GroupAction> getActionsByGroupIdEquals(long groupId);
}
