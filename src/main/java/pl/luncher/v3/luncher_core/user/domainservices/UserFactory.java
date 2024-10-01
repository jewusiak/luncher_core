package pl.luncher.v3.luncher_core.user.domainservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {

//  public Page<User> findByStringQueryPaged(String query, PageRequest pageRequest) {
//    if (query == null || query.isBlank()) {
//      return userRepository.findAll(pageRequest).map(this::of);
//    }
//
//    SearchSession session = Search.session(entityManager);
//
//    SearchResult<UserDb> result = session.search(UserDb.class)
//        .where(f -> f.match().fields("firstname", "surname", "email").matching(query).fuzzy(2))
//        .fetch(pageRequest.getPageSize() * pageRequest.getPageNumber(), pageRequest.getPageSize());
//
//    return new PageImpl<>(result.hits(), pageRequest, result.total().hitCount()).map(this::of);
//  }
//  
}
