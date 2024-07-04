package com.cydeo.controller;


import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getProjects() {

        List<ProjectDTO> projects = projectService.listAllProjects();

        return ResponseEntity.ok( new ResponseWrapper("projects are successfully retrieved",projects, HttpStatus.OK));

    }

    @GetMapping("{projectCode}")
    public ResponseEntity<ResponseWrapper> getProjectByUserCode(@PathVariable("projectCode") String projectCode1) {

        ProjectDTO projectDTO =projectService.getByProjectCode(projectCode1);

        return ResponseEntity.ok( new ResponseWrapper("project is successfully retrieved",projectDTO, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO) {

        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("project is created",projectDTO,HttpStatus.CREATED));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO) {
        projectService.update(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("project updated",HttpStatus.OK));
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable ("projectId") String projectCode) {
        projectService.delete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("project is deleted",HttpStatus.OK));
    }

    @GetMapping("/manager/project-status")
    public ResponseEntity<ResponseWrapper> getProjectByManager(){

        List<ProjectDTO> projectList = projectService.listAllProjectDetails();
        return ResponseEntity.ok(new ResponseWrapper("project is successfully retrieved",projectList,HttpStatus.OK));


    }


    @PutMapping("/manager/complete/{projectCode}")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode") String projectCode){
        projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully completed", HttpStatus.OK));

    }





}
