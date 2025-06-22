package com.app.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.dto.IdentifyResponse;
import com.app.entity.Contact;
import com.app.entity.LinkPrecedence;
import com.app.repository.ContactRepository;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepo;

    public IdentifyResponse identify(String email, String phoneNumber) {
        List<Contact> matches = contactRepo.findByEmailOrPhoneNumber(email, phoneNumber);

        // Case: No matches — create new PRIMARY contact
        if (matches.isEmpty()) {
            Contact contact = new Contact();
            contact.setEmail(email);
            contact.setPhoneNumber(phoneNumber);
            contact.setLinkPrecedence(LinkPrecedence.PRIMARY); // ✅ enum
            contactRepo.save(contact);

            return new IdentifyResponse(contact.getId(), List.of(email), List.of(phoneNumber), List.of());
        }

        // Find the primary contact (if any), or pick the first one
        Contact primary = matches.stream()
                .filter(c -> c.getLinkPrecedence() == LinkPrecedence.PRIMARY)
                .findFirst()
                .orElse(matches.get(0));

        Set<String> emails = new HashSet<>();
        Set<String> phones = new HashSet<>();
        List<Long> secondaryIds = new ArrayList<>();

        for (Contact contact : matches) {
            if (contact.getEmail() != null) emails.add(contact.getEmail());
            if (contact.getPhoneNumber() != null) phones.add(contact.getPhoneNumber());

            // Add to secondary list if not primary
            if (!Objects.equals(contact.getId(), primary.getId())) {
                secondaryIds.add(contact.getId());
            }
        }

        // Check if this request introduces new data
        boolean isNewData = matches.stream()
                .noneMatch(c -> 
                    (email != null && email.equalsIgnoreCase(c.getEmail())) ||
                    (phoneNumber != null && phoneNumber.equals(c.getPhoneNumber()))
                );

        // If new email or phoneNumber is introduced, create secondary contact
        if (isNewData) {
            Contact secondary = new Contact();
            secondary.setEmail(email);
            secondary.setPhoneNumber(phoneNumber);
            secondary.setLinkPrecedence(LinkPrecedence.SECONDARY); // ✅ enum
            secondary.setLinkedId(primary.getId());
            contactRepo.save(secondary);

            secondaryIds.add(secondary.getId());

            if (secondary.getEmail() != null) emails.add(secondary.getEmail());
            if (secondary.getPhoneNumber() != null) phones.add(secondary.getPhoneNumber());
        }

        return new IdentifyResponse(
                primary.getId(),
                new ArrayList<>(emails),
                new ArrayList<>(phones),
                secondaryIds
        );
    }
}
