package newangle.artifex.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import newangle.artifex.domain.agent.AiAgent;
import newangle.artifex.services.AiAgentService;

@RestController
public class AiAgentController {
    
    @Autowired
    private AiAgentService service;

    @PostMapping(value="/create-agent")
    public ResponseEntity<AiAgent> createAiAgent(@RequestBody AiAgent aiAgent) {
        aiAgent = service.create(aiAgent);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(aiAgent.getId()).toUri();
        return ResponseEntity.created(uri).body(aiAgent);
    }

    @GetMapping(value="/agents/{id}")
    public ResponseEntity<AiAgent> findById(@PathVariable UUID id) {
        AiAgent aiAgent = service.findById(id);
        return ResponseEntity.ok().body(aiAgent);
    }

    @DeleteMapping(value="/agents/delete-agent/{id}")
    public ResponseEntity<AiAgent> deleteAgent(@PathVariable UUID id) {
        service.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value="/agents/update-agent/{id}")
    public ResponseEntity<AiAgent> updateAgent(@PathVariable UUID id, @RequestBody AiAgent aiAgent) {
        aiAgent = service.updateAgent(id, aiAgent);
        return ResponseEntity.ok().body(aiAgent);
    }

}