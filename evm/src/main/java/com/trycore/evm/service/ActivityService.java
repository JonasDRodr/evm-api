package com.trycore.evm.service;

import com.trycore.evm.dto.ActivityEvmDto;
import com.trycore.evm.dto.ActivityRequestDto;
import com.trycore.evm.model.Activity;
import com.trycore.evm.model.Project;
import com.trycore.evm.repository.ActivityRepository;
import com.trycore.evm.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ProjectRepository projectRepository;
    private final EvmCalculatorService evmCalculatorService;

    public List<ActivityEvmDto> findByProjectId(Long projectId) {
        return activityRepository.findByProjectId(projectId)
                .stream()
                .map(evmCalculatorService::calculate)
                .toList();
    }

    public ActivityEvmDto create(Long projectId, ActivityRequestDto request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        Activity activity = new Activity();
        activity.setProject(project);
        activity.setName(request.getName());
        activity.setBac(request.getBac());
        activity.setPlannedProgress(request.getPlannedProgress());
        activity.setActualProgress(request.getActualProgress());
        activity.setActualCost(request.getActualCost());

        return evmCalculatorService.calculate(activityRepository.save(activity));
    }

    public ActivityEvmDto update(Long id, ActivityRequestDto request) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id: " + id));

        activity.setName(request.getName());
        activity.setBac(request.getBac());
        activity.setPlannedProgress(request.getPlannedProgress());
        activity.setActualProgress(request.getActualProgress());
        activity.setActualCost(request.getActualCost());

        return evmCalculatorService.calculate(activityRepository.save(activity));
    }

    public void delete(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new EntityNotFoundException("Activity not found with id: " + id);
        }
        activityRepository.deleteById(id);
    }
}