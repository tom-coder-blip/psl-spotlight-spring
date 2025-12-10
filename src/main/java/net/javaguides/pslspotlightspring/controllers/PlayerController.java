package net.javaguides.pslspotlightspring.controllers;

import net.javaguides.pslspotlightspring.entities.Player;
import net.javaguides.pslspotlightspring.services.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPlayer(
            @RequestParam("name") String name,
            @RequestParam("club") String club,
            @RequestParam("position") String position,
            @RequestParam(value = "playerPicture", required = false) MultipartFile playerPicture
    ) {
        try {
            String filename = null;

            if (playerPicture != null && !playerPicture.isEmpty()) {
                filename = System.currentTimeMillis() + "_" + playerPicture.getOriginalFilename();

                Path uploadDir = Paths.get("uploads");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                Path path = uploadDir.resolve(filename);
                Files.copy(playerPicture.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            Player player = Player.builder()
                    .name(name)
                    .club(club)
                    .position(position)
                    .playerPicture(filename)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(playerService.createPlayer(player));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Player>> getPlayers() {
        return ResponseEntity.ok(playerService.getPlayers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayerStats(@PathVariable Long id, @RequestBody Player updated) {
        return ResponseEntity.ok(playerService.updatePlayerStats(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        try {
            playerService.deletePlayer(id);
            return ResponseEntity.ok().body("Player deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
        }
    }
}