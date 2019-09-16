package com.kosmoastronauta.newsletter.repository;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmailRepository extends CrudRepository<EmailAddress, Long>
{
     List<EmailAddress> getEmailAddressesByActiveIsTrue();

     EmailAddress getEmailAddressesByIdEquals(long id);

     EmailAddress getEmailAddressesByPubKeyEquals(String pubKey);
}
