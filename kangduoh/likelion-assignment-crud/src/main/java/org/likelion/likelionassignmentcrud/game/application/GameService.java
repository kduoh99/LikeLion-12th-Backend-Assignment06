package org.likelion.likelionassignmentcrud.game.application;

import org.likelion.likelionassignmentcrud.developer.domain.Developer;
import org.likelion.likelionassignmentcrud.developer.domain.repository.DeveloperRepository;
import org.likelion.likelionassignmentcrud.game.api.dto.request.GameSaveReqDto;
import org.likelion.likelionassignmentcrud.game.api.dto.request.GameUpdateReqDto;
import org.likelion.likelionassignmentcrud.game.api.dto.response.GameInfoResDto;
import org.likelion.likelionassignmentcrud.game.api.dto.response.GameListResDto;
import org.likelion.likelionassignmentcrud.game.domain.Game;
import org.likelion.likelionassignmentcrud.game.domain.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GameService {
    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;

    public GameService(GameRepository gameRepository, DeveloperRepository developerRepository) {
        this.gameRepository = gameRepository;
        this.developerRepository = developerRepository;
    }

    // Create
    @Transactional
    public void gameSave(GameSaveReqDto gameSaveReqDto) {
        Developer developer = developerRepository.findById(gameSaveReqDto.developerId())
                .orElseThrow(IllegalArgumentException::new);

        Game game = Game.builder()
                .name(gameSaveReqDto.name())
                .genre(gameSaveReqDto.genre())
                .platform(gameSaveReqDto.platform())
                .developer(developer)
                .build();

        gameRepository.save(game);
    }

    // 개발사에 따른 게임 리스트 조회
    public GameListResDto gameFindAllByDeveloperName(String developerName) {
        Developer developer = developerRepository.findByName(developerName)
                .orElseThrow(() -> new IllegalArgumentException("해당 개발사를 찾을 수 없습니다."));

        List<Game> games = gameRepository.findByDeveloper(developer);
        List<GameInfoResDto> gameInfoResDtoList = games.stream()
                .map(GameInfoResDto::from)
                .toList();

        return GameListResDto.from(gameInfoResDtoList);
    }

    // Update
    @Transactional
    public void gameUpdate(Long gameId, GameUpdateReqDto gameUpdateReqDto) {
        Game game = gameRepository.findById(gameId).orElseThrow(IllegalArgumentException::new);

        game.update(gameUpdateReqDto);
    }

    // Delete
    @Transactional
    public void gameDelete(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(IllegalArgumentException::new);

        gameRepository.delete(game);
    }
}
