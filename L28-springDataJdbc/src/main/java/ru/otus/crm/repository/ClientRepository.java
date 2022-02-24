package ru.otus.crm.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;


public interface ClientRepository extends CrudRepository<Client, Long> {


    List<Client> findByName(String name);

    @Query("select * from client where upper(name) = upper(:name)")
    Optional<Client> findByNameIgnoreCase(@Param("name") String name);

    @Modifying
    @Query("update client set name = :newName where id = :id")
    void updateName(@Param("id") long id, @Param("newName") String newName);

    @Override
    @Query(value = """
            select  c.id client_id,
                    c.name client_name,                    
                    a.street,
                    p.id phone_id,
                    p.number
            from client c
                     left outer join address a
                                     on a.client_id = c.id
                     left outer join phones p on p.client_id=c.id 
                     order by c.id
                                                          """,
            resultSetExtractorClass = ClientResultSetExtractorClass.class)
    List<Client> findAll();
}
