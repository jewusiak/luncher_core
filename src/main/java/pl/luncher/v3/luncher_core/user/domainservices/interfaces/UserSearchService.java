package pl.luncher.v3.luncher_core.user.domainservices.interfaces;

import java.util.List;
import pl.luncher.v3.luncher_core.user.model.User;

public interface UserSearchService {

  List<User> search(String query, int page, int size);
  void reindexDb() throws InterruptedException;
}
