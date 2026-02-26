package com.example.grannfix.offer.repository;

import com.example.grannfix.offer.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {
}
