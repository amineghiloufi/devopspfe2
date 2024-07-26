package com.example.demoo.controllers;

import com.example.demoo.domain.Contravention;
import com.example.demoo.domain.HttpResponse;
import com.example.demoo.services.ContraventionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contraventions")
public class ContraventionController {

    private final ContraventionService contraventionService;

    public ContraventionController(ContraventionService contraventionService) {
        this.contraventionService = contraventionService;
    }

    @GetMapping
    public List<Contravention> getAllContraventions() {
        return contraventionService.findAll();
    }

    @GetMapping("/{id}")
    public Contravention getContraventionById(@PathVariable Long id) {

        return contraventionService.findById(id);
    }

    @PostMapping("/add")
    public Contravention addContravention(@RequestParam("customerFullName") String customerFullName,
                                          @RequestParam("description") String contraventionDescription) {
        return contraventionService.addContravention(customerFullName, contraventionDescription);
    }

    @PutMapping("/update")
    public Contravention updateContravention(@RequestParam("currentContraventionId") Long currentContraventionId,
                                             @RequestParam("active") String active) {
        return contraventionService.update(currentContraventionId, Boolean.parseBoolean(active));
    }

    @DeleteMapping("/delete/{contraventionId}")
    public ResponseEntity<HttpResponse> deleteById(@PathVariable("contraventionId") Long contraventionId) {
        contraventionService.deleteById(contraventionId);
        return response(HttpStatus.OK, "Item is successfully deleted");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse(httpStatus.value(),
                httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(),
                message);
        return new ResponseEntity<>(body, httpStatus);
    }
}
