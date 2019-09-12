package com.kosmoastronauta.newsletter.repository;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends CrudRepository<EmailAddress, Long>
{
    public List<EmailAddress> getEmailAddressesByGroupEmailEquals(String groupEmail);

    public List<EmailAddress> getEmailAddressesByActiveIsTrue();

    public List<EmailAddress> getEmailAddressesByGroupIdEquals(int groupId);
}
