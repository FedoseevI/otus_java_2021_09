package ru.otus.crm.formInput;

import lombok.*;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
public class ClientInput {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String address;
    @Getter
    @Setter
    private String phones;

    public Client toClient() {
        var resultClient = new Client(null, this.name, null, null);
        if (phones != null) {
            var phones = Arrays.stream(this.phones.split(";")).map(e -> new Phone(e, null)).collect(Collectors.toSet());
            resultClient.setPhones(phones);
        }
        if (address != null) resultClient.setAddress(new Address(address, 1L));
        return resultClient;
    }
}
