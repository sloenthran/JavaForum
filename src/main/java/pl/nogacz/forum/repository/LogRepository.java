package pl.nogacz.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nogacz.forum.domain.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {}