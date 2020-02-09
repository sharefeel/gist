package net.youngrok.gist;

import net.youngrok.gist.protos.AddressBookMessage.*;
import net.youngrok.gist.protos.AddressBookMessage.Person.PhoneNumber;
import net.youngrok.gist.protos.AddressBookMessage.Person.PhoneType;
import org.junit.Test;

import java.io.*;

public class ProtocolBuffers {
    private static final String SERIALIZED_ADDRESS_BOOK = "addressbook.message";

    @Test
    public void writeMessage() {
        // Two persons
        Person rock = Person.newBuilder().setName("rock").setId(32).setEmail("rock@nroll.com")
                .addPhones(PhoneNumber.newBuilder().setNumber("010-1024-2048").setType(PhoneType.MOBILE).build())
                .addPhones(PhoneNumber.newBuilder().setNumber("02-3273-8783")).build();
        Person kai = Person.newBuilder().setName("kai").setId(33).setEmail("kai@database.org")
                .addPhones(PhoneNumber.newBuilder().setNumber("010-1677-7216").setType(PhoneType.MOBILE).build())
                .build();

        // Addressbook
        AddressBook addressBook = AddressBook.newBuilder().addPeople(rock).addPeople(kai).build();

        // Write to file
        try (OutputStream outputStream = new FileOutputStream(SERIALIZED_ADDRESS_BOOK)) {
            addressBook.writeTo(outputStream);
        } catch (IOException ignore) {
        }
    }

    @Test
    public void readMessage() {
        try (InputStream inputStream = new FileInputStream(SERIALIZED_ADDRESS_BOOK)) {
            AddressBook addressBook = AddressBook.parseFrom(inputStream);
            System.out.println(addressBook.toString());
        } catch (IOException ignore) {
        }
    }

    @Test
    public void loopAddressBook() {
        try (InputStream inputStream = new FileInputStream(SERIALIZED_ADDRESS_BOOK)) {
            AddressBook addressBook = AddressBook.parseFrom(inputStream);
            for (Person person : addressBook.getPeopleList()) {
                System.out.println("Person ID: " + person.getId());
                System.out.println("  Name: " + person.getName());
                if (person.hasEmail()) {
                    System.out.println("  E-mail address: " + person.getEmail());
                }

                for (Person.PhoneNumber phoneNumber : person.getPhonesList()) {
                    switch (phoneNumber.getType()) {
                        case MOBILE:
                            System.out.print("  Mobile phone #: ");
                            break;
                        case HOME:
                            System.out.print("  Home phone #: ");
                            break;
                        case WORK:
                            System.out.print("  Work phone #: ");
                            break;
                    }
                    System.out.println(phoneNumber.getNumber());
                }
            }
        } catch (IOException ignore) {
        }
    }
}
