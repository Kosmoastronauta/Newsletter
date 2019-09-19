package com.kosmoastronauta.newsletter.services;

import com.kosmoastronauta.newsletter.domain.GroupAction;
import com.kosmoastronauta.newsletter.repository.ActionRepository;
import com.kosmoastronauta.newsletter.repository.EmailGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ActionService
{
    @Autowired
    ActionRepository actionRepository;

    @Autowired
    EmailGroupRepository emailGroupRepository;

    public List<GroupAction> getAllActionsByGroupId(long groupId) throws NoSuchFieldException, NoSuchElementException
    {
        if(!emailGroupRepository.existsById(groupId)) throw new NoSuchFieldException("There is no group with that id!");

        List<GroupAction> groupActions = actionRepository.getActionsByGroupIdEquals(groupId);

        if(groupActions.isEmpty())
            throw new NoSuchElementException("There is no actions for group with that id!");

        else return groupActions;
    }

    public void deleteActionByActionIdAndGroupId(long actionId, long groupId) throws NoSuchFieldException
    {
        if(!actionRepository.existsGroupActionByIdEqualsAndGroupIdEquals(actionId,groupId))
            throw new NoSuchFieldException("There does not exist action with that id for that group Id");

        actionRepository.deleteById(actionId);
    }

    public void addActionForGroup(GroupAction groupAction) throws NoSuchFieldException,InvalidParameterException
    {
        if(!isActionValid(groupAction)) throw new InvalidParameterException("Invalid data!");

        groupAction.setId(0);
        if(!emailGroupRepository.existsById(groupAction.getGroupId()))
            throw new NoSuchFieldException("There is no group with that id!");

        actionRepository.save(groupAction);
    }

    private boolean isActionValid(GroupAction groupAction)
    {
        // if any of necessary field is empty
        return  !isSpaceInName(groupAction.getName()) && groupAction.getGroupId() != 0 && !groupAction.getName().isEmpty() && !groupAction.getContent().isEmpty() && !groupAction.getSubject().isEmpty();
    }

    private boolean isSpaceInName(String name)
    {
        for(int i = 0; i < name.length(); i++)
        {
            if(name.charAt(i) == ' ') return true;
        }
        return false;
    }
}
