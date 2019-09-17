package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.Action;
import com.kosmoastronauta.newsletter.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionService
{
    @Autowired
    ActionRepository actionRepository;

    public List<Action> getAllActionsByGroupId(long groupId) throws NoSuchFieldException
    {
        List<Action> actions = actionRepository.getActionsByGroupIdEquals(groupId);

        if(actions == null)
            throw new NoSuchFieldException("There is no actions for group with that id");

        else return actions;
    }
}
