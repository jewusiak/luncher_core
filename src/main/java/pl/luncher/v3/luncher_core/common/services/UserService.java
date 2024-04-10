package pl.luncher.v3.luncher_core.common.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.admin.model.mappers.AdminUserMapper;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminUpdateUserRequest;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.exceptions.InsufficientRoleException;
import pl.luncher.v3.luncher_core.common.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final AdminUserMapper adminUserMapper;

    public UserDetails getUserDetailsByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user, UUID uuid) {
        user.setUuid(uuid);
        return userRepository.save(user);
    }

    public void checkIfUserCanChangeOtherUser(User user, User changedBy) {
        if (changedBy.getRole().compareRoleTo(user.getRole()) <= 0 && changedBy.getRole() != AppRole.SYS_ROOT)
            throw new InsufficientRoleException("Created/updated user cannot have equal or higher role than the creator.");
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Page<User> getAllUsersPaged(PageRequest request) {
        return userRepository.findUsersByOrderBySequence(request);
    }

    public User getUserByUuid(UUID userUuid) {
        return userRepository.findUserByUuid(userUuid).orElse(null);
    }

    public Page<User> findByStringQueryPaged(String query, PageRequest pageRequest) {
        if (query == null || query.isBlank())
            return getAllUsersPaged(pageRequest);

        SearchSession session = Search.session(entityManager);

        SearchResult<User> result = session.search(User.class).where(f ->
                        f.match().fields("firstname", "surname", "email").matching(query).fuzzy(2))
                .fetch(pageRequest.getPageSize() * pageRequest.getPageNumber(), pageRequest.getPageSize());

        return new PageImpl<>(result.hits(), pageRequest, result.total().hitCount());
    }

    public User mapToUpdateUserByAdmin(AdminUpdateUserRequest request, String userId) {
        var oldUser = userRepository.findUserByUuid(UUID.fromString(userId)).orElseThrow(EntityNotFoundException::new);
        adminUserMapper.mapToUpdateUser(oldUser, request, UUID.fromString(userId));
        return oldUser;
    }
}
