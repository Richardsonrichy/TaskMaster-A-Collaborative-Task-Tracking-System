package com.taskmaster.controller;

import com.taskmaster.dto.TeamRequest;
import com.taskmaster.dto.TeamResponse;
import com.taskmaster.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public TeamResponse createTeam(@Valid @RequestBody TeamRequest request) {

        return teamService.createTeam(request);
    }

    @GetMapping
    public List<TeamResponse> getAllTeams() {

        return teamService.getAllTeams();
    }

    @PostMapping("/{teamId}/join")
    public TeamResponse joinTeam(@PathVariable Long teamId) {

        return teamService.joinTeam(teamId);
    }
}