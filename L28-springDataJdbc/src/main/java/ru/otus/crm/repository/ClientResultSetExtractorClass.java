package ru.otus.crm.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientList = new ArrayList<Client>();
        String prevClientId = null;
        String prevPhoneId = null;
        Client client = null;
        while (rs.next()) {
            var clientId = rs.getString("client_id");
            if (prevClientId == null || !prevClientId.equals(clientId)) {
                client = new Client(Long.parseLong(clientId), rs.getString("client_name"), new Address(rs.getString("street"), Long.parseLong(clientId)), new HashSet<>());
                clientList.add(client);
                prevClientId = clientId;
            }
            Long phoneId = (Long) rs.getObject("phone_id");
            if (client != null && phoneId != null && (prevPhoneId == null || !prevPhoneId.equals(phoneId.toString()))) {
                client.getPhones().add(new Phone(phoneId, rs.getString("number"), Long.parseLong(clientId)));
                prevPhoneId = phoneId.toString();
            }
        }
        return clientList;
    }
}
