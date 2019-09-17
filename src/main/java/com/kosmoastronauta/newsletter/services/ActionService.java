package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.Action;
import com.kosmoastronauta.newsletter.domain.EmailGroup;
import com.kosmoastronauta.newsletter.repository.ActionRepository;
import com.kosmoastronauta.newsletter.repository.EmailGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class ActionService
{
    @Autowired
    ActionRepository actionRepository;

    @Autowired
    EmailGroupRepository emailGroupRepository;

    public List<Action> getAllActionsByGroupId(long groupId) throws NoSuchFieldException
    {
        List<Action> actions = actionRepository.getActionsByGroupIdEquals(groupId);

        if(actions == null)
            throw new NoSuchFieldException("There is no actions for group with that id");

        else return actions;
    }

    public void addActionForGroup(Action action) throws NoSuchFieldException,InvalidParameterException
    {
        if(!isActionValid(action)) throw new InvalidParameterException("Invalid data!");

        if(!emailGroupRepository.existsById(action.getGroupId()))
            throw new NoSuchFieldException("There is no group with that id!");

        actionRepository.save(action);
    }

    private boolean isActionValid(Action action)
    {
        // if any of necessary field is empty
        return action.getGroupId() != 0 && !action.getName().isEmpty() && !action.getSchema().isEmpty();
    }
}
