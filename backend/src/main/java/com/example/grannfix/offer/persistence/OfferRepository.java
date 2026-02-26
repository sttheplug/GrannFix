package com.example.grannfix.offer.persistence;

import com.example.grannfix.offer.domain.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {
}
