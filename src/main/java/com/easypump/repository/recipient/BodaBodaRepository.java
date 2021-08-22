package com.easypump.repository.recipient;

import com.easypump.model.bodaboda.BodaBoda;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BodaBodaRepository extends JpaRepository<BodaBoda, Integer> {

    @NotNull
    @Override
    @Query(value = "SELECT * FROM boda_boda bb " +
            "WHERE bb.record_status = 'ACTIVE' AND bb.id = :id",
            nativeQuery = true)
    Optional<BodaBoda> findById(@NotNull Integer id);
}
