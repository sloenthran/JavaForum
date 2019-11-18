package pl.nogacz.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.nogacz.forum.domain.Log;

public interface LogRepository extends JpaRepository<Log, Long> {}