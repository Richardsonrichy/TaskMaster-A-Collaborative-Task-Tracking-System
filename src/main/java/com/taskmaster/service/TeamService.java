package com.taskmaster.service;

import com.taskmaster.dto.TeamRequest;
import com.taskmaster.dto.TeamResponse;
import com.taskmaster.entity.Team;
import com.taskmaster.entity.User;
import com.taskmaster.repository.TeamRepository;
import com.taskmaster.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository,
                       UserRepository userRepository) {

        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

public TeamResponse createTeam(TeamRequest request) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    User user = (User) authentication.getPrincipal();

    Team team = new Team();

    team.setName(request.getName());
    team.setDescription(request.getDescription());

    List<User> members = new ArrayList<>();
    members.add(user);

    team.setMembers(members);

    Team savedTeam = teamRepository.save(team);

    TeamResponse response = new TeamResponse();

    response.setId(savedTeam.getId());
    response.setName(savedTeam.getName());
    response.setDescription(savedTeam.getDescription());

    return response;
}

public List<TeamResponse> getAllTeams() {

    List<Team> teams = teamRepository.findAll();

    return teams.stream().map(team -> {

        TeamResponse response = new TeamResponse();

        response.setId(team.getId());
        response.setName(team.getName());
        response.setDescription(team.getDescription());

        return response;

    }).toList();
}

public TeamResponse joinTeam(Long teamId) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    User user = (User) authentication.getPrincipal();

    Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

    if (!team.getMembers().contains(user)) {
        team.getMembers().add(user);
    }

    Team savedTeam = teamRepository.save(team);

    TeamResponse response = new TeamResponse();

    response.setId(savedTeam.getId());
    response.setName(savedTeam.getName());
    response.setDescription(savedTeam.getDescription());

    return response;
}

}