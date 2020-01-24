package com.kosmoastronauta.newsletter.repository;

import com.kosmoastronauta.newsletter.domain.EmailToGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmailToGroupRepository extends CrudRepository<EmailToGroup,Long>
{
    EmailToGroup getEmailToGroupByEmailIdEqualsAndGroupIdEquals(long emailID, long groupId);

    List<EmailToGroup> getEmailToGroupByGroupIdEqualsAndActiveTrue(long groupId);

    boolean existsEmailToGroupByEmailIdEqualsAndActiveTrue(long emailId);

    @Query(value = "SELECT email_to_group.id, email_to_group.email_id, email_to_group.group_id, email_to_group.active" +
            " " +
            "FROM " +
            "email_to_group INNER JOIN email_address ON " +
            "email_to_group.email_id=email_address.id " +
            "INNER JOIN email_group ON " +
            "email_group.id=email_to_group.group_id " +
            "WHERE " + "email_address.address=:address " +
            "AND " + "email_group.name=:groupName", nativeQuery = true)
    EmailToGroup getEmailToGroupByEmailAddressEqualsAndGroupNameEquals(String address, String groupName);
}
