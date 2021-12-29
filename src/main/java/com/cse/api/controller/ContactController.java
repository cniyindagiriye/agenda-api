package com.cse.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.cse.api.exception.ResourceNotFoundException;
import com.cse.api.model.Contact;
import com.cse.api.repository.ContactRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ContactController {

  @Autowired
  private ContactRepository contactRepository;

  @PostMapping("/contacts")
  public Contact createContact(@Valid @RequestBody Contact contact) throws ResourceNotFoundException {
    return contactRepository.save(contact);
  }

  @GetMapping("/contacts")
  public List<Contact> getAllContacts() {
    return contactRepository.findAll();
  }

  @GetMapping("/contacts/{id}")
  public ResponseEntity<Contact> getContactById(@PathVariable(value = "id") Long contactId)
      throws ResourceNotFoundException {
    Contact contact = contactRepository.findById(contactId)
        .orElseThrow(() -> new ResourceNotFoundException("Contact not found :: " + contactId));
    return ResponseEntity.ok().body(contact);
  }

  @DeleteMapping("/contacts/{id}")
  public Map<String, Boolean> deletContact(@PathVariable(value = "id") Long contactId)
      throws ResourceNotFoundException {
    Contact contact = contactRepository.findById(contactId)
        .orElseThrow(() -> new ResourceNotFoundException("Contact not found :: " + contactId));
    contactRepository.delete(contact);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }

  @PutMapping("/contacts/{id}")
  public ResponseEntity<Contact> updateContact(@PathVariable(value = "id") Long contactId,
      @Valid @RequestBody Contact contactDetails) throws ResourceNotFoundException {
    Contact contact = contactRepository.findById(contactId)
        .orElseThrow(() -> new ResourceNotFoundException("Contact not found :: " + contactId));
    contact.setPhoto(contactDetails.getPhoto());
    contact.setPhoneNumber(contactDetails.getPhoneNumber());
    contact.setFirstName(contactDetails.getFirstName());
    contact.setLastName(contactDetails.getLastName());
    final Contact updatedContact = contactRepository.save(contact);
    return ResponseEntity.ok(updatedContact);
  }

}
