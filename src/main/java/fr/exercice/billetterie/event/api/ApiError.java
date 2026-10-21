package fr.exercice.billetterie.event.api;

import java.util.Map;

public record ApiError(String message, Map<String, String> fieldErrors) {
}
