package dev.supportkb.search;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchQueryLogRepository extends JpaRepository<SearchQueryLog, Long> {

  List<SearchQueryLog> findTop20ByResultCountOrderBySearchedAtDesc(long resultCount);
}
