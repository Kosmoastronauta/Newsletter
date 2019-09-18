package com.kosmoastronauta.newsletter.repository;

import com.kosmoastronauta.newsletter.domain.GroupAction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRepository extends CrudRepository<GroupAction, Long>
{
    List<GroupAction> getActionsByGroupIdEquals(long groupId);

    @Query(value = "SELECT email_address.address, email_to_group.group_id, group_action.subject, group_action.content" +
            " " +
            "FROM " + "email_address INNER JOIN email_to_group " +
            "ON " +
                    "email_address.id = email_to_group.email_id " +
                    "INNER JOIN group_action " +
                    "ON " +
                    "group_action.group_id = email_to_group.group_id " +
                    "WHERE " +
                    "email_to_group.active = true " +
                    "AND " +
                    "group_action.name =:actionName " +
                    "AND " +
                    "email_to_group.group_id=:groupId", nativeQuery = true)

    List<Object[]> getListOfActiveAddressesGroupIdSubjectsAndContentByActionName(long groupId, String actionName);
}
