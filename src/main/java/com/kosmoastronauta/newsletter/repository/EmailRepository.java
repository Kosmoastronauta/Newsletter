package com.kosmoastronauta.newsletter.repository;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends CrudRepository<EmailAddress, Long>
{
     EmailAddress getEmailAddressesByIdEquals(long id);

     EmailAddress getEmailAddressByPubKeyEquals(String pubKey);

     EmailAddress getEmailAddressByAddressEquals(String address);
}
