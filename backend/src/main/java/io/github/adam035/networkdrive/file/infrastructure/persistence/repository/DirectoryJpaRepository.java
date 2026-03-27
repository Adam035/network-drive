package io.github.adam035.networkdrive.file.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.DirectoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface DirectoryJpaRepository extends JpaRepository<DirectoryEntity, String> {

    @Query(value = """
    WITH RECURSIVE children AS (
        SELECT id, parent_id FROM storage_resources WHERE parent_id = :rootId
        UNION ALL
        SELECT s.id, s.parent_id
        FROM storage_resources s
        JOIN children c ON s.parent_id = c.id
    )
    SELECT id FROM children
    """, nativeQuery = true)
    List<String> findAllChildIds(@Param("rootId") String rootId);

}
