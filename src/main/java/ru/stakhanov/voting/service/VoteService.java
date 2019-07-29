package ru.stakhanov.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stakhanov.voting.model.Menu;
import ru.stakhanov.voting.model.Vote;
import ru.stakhanov.voting.repository.UserRepository;
import ru.stakhanov.voting.repository.VoteRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<Vote> findByUserAndDate(int userId, LocalDate date) {
        return voteRepository.findByUserAndDate(userId, date);
    }

    @Transactional
    public VoteWithStatus save(int userId, final Menu menu) {
        LocalDate date = menu.getDate();
        VoteWithStatus voteWithStatus = voteRepository.findByUserAndDate(userId, date)
                .map(v -> {
                    v.setMenu(menu);
                    return new VoteWithStatus(v, false);
                })
                .orElseGet(() -> new VoteWithStatus(
                        new Vote(userRepository.getOne(userId), menu, date), true));

        voteRepository.save(voteWithStatus.getVote());
        return voteWithStatus;
    }

    @Transactional
    public VoteWithStatus saveIfAbsent(int userId, final Menu menu) {
        LocalDate date = menu.getDate();
        return voteRepository.findByUserAndDate(userId, date)
                .map(v -> new VoteWithStatus(v, false))
                .orElseGet(() -> new VoteWithStatus(voteRepository.save(new Vote(userRepository.getOne(userId), menu, date)), true));
    }

    public static class VoteWithStatus {
        private final Vote vote;
        private final boolean created;

        public VoteWithStatus(Vote vote, boolean updated) {
            this.vote = vote;
            this.created = updated;
        }

        public Vote getVote() {
            return vote;
        }

        public boolean isCreated() {
            return created;
        }
    }
}