import { HttpErrorResponse } from '@angular/common/http';
import { ApiProblem } from './api.models';

/** Extrait le ProblemDetail d'une erreur HTTP, s'il y en a un. */
export function toApiProblem(error: unknown): ApiProblem | null {
  if (error instanceof HttpErrorResponse && error.error && typeof error.error === 'object') {
    return error.error as ApiProblem;
  }
  return null;
}

/** Message lisible pour l'utilisateur, quelle que soit la forme de l'erreur. */
export function errorMessage(error: unknown): string {
  if (error instanceof HttpErrorResponse && error.status === 0) {
    return "Impossible de joindre le serveur. Vérifie que l'API est démarrée.";
  }
  return toApiProblem(error)?.detail ?? 'Une erreur inattendue est survenue.';
}
