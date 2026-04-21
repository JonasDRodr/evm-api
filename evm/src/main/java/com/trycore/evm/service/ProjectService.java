package com.trycore.evm.service;

import com.trycore.evm.dto.ActivityEvmDto;
import com.trycore.evm.dto.ProjectEvmDto;
import com.trycore.evm.dto.ProjectRequestDto;
import com.trycore.evm.model.Project;
import com.trycore.evm.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private final ProjectRepository projectRepository;
    private final EvmCalculatorService evmCalculatorService;

    public List<ProjectEvmDto> findAll() {
        return projectRepository.findAll()
                .stream()
                .map(this::toProjectEvmDto)
                .toList();
    }

    public ProjectEvmDto findById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        return toProjectEvmDto(project);
    }

    public ProjectEvmDto create(ProjectRequestDto request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        return toProjectEvmDto(projectRepository.save(project));
    }

    public ProjectEvmDto update(Long id, ProjectRequestDto request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        return toProjectEvmDto(projectRepository.save(project));
    }

    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    private ProjectEvmDto toProjectEvmDto(Project project) {
        List<ActivityEvmDto> activityEvmDtos = project.getActivities() == null
                ? List.of()
                : project.getActivities()
                        .stream()
                        .map(evmCalculatorService::calculate)
                        .toList();

        return evmCalculatorService.calculateProjectSummary(project, activityEvmDtos);
    }
}