package com.trycore.evm.controller;

import com.trycore.evm.dto.ActivityEvmDto;
import com.trycore.evm.dto.ActivityRequestDto;
import com.trycore.evm.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/activities")
@RequiredArgsConstructor
@Tag(name = "Activities", description = "Activity management with EVM indicators")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    @Operation(summary = "Get all activities for a project with EVM indicators")
    public ResponseEntity<List<ActivityEvmDto>> findByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(activityService.findByProjectId(projectId));
    }

    @PostMapping
    @Operation(summary = "Create a new activity for a project")
    public ResponseEntity<ActivityEvmDto> create(@PathVariable Long projectId,
            @Valid @RequestBody ActivityRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.create(projectId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing activity")
    public ResponseEntity<ActivityEvmDto> update(@PathVariable Long projectId,
            @PathVariable Long id,
            @Valid @RequestBody ActivityRequestDto request) {
        return ResponseEntity.ok(activityService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an activity")
    public ResponseEntity<Void> delete(@PathVariable Long projectId, @PathVariable Long id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}