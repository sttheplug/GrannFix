package com.example.grannfix.common.contracts;
import java.util.UUID;

public record TaskOfferView(UUID id, UUID createdById, boolean offerable) {}