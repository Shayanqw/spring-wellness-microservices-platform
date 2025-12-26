package ca.gbc.goaltrackingservice.controller;

import ca.gbc.goaltrackingservice.model.Goal;
import ca.gbc.goaltrackingservice.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    // INTER-SERVICE COMMUNICATION ENDPOINT
    @GetMapping("/{goalId}/suggested-resources")
    public List<Object> getSuggestedResources(@PathVariable String goalId) {
        return goalService.suggestResourcesForGoal(goalId);
    }

    // REGULAR CRUD ENDPOINTS
    @GetMapping
    public List<Goal> getAllGoals() {
        return goalService.getAllGoals();
    }

    @GetMapping("/{id}")
    public Goal getGoal(@PathVariable String id) {
        return goalService.getGoalById(id);
    }

    @PostMapping
    public Goal createGoal(@RequestBody Goal goal) {
        return goalService.createGoal(goal);
    }

    @PutMapping("/{id}")
    public Goal updateGoal(@PathVariable String id, @RequestBody Goal goal) {
        return goalService.updateGoal(id, goal);
    }

    @DeleteMapping("/{id}")
    public void deleteGoal(@PathVariable String id) {
        goalService.deleteGoal(id);
    }

    @PatchMapping("/{id}/complete")
    public Goal markGoalAsCompleted(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        String username = (jwt != null && jwt.getClaimAsString("preferred_username") != null)
                ? jwt.getClaimAsString("preferred_username")
                : (jwt != null ? jwt.getSubject() : "unknown");
        return goalService.markAsCompleted(id, username);
    }

    @GetMapping("/status/{status}")
    public List<Goal> getGoalsByStatus(@PathVariable String status) {
        return goalService.getGoalsByStatus(status);
    }

    @GetMapping("/category/{category}")
    public List<Goal> getGoalsByCategory(@PathVariable String category) {
        return goalService.getGoalsByCategory(category);
    }
}