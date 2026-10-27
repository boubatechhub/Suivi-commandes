/** Liste paginée renvoyée par l'API (voir PageResponse côté backend). */
export interface Page<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

/** Corps d'erreur standard de l'API (ProblemDetail, RFC 9457). */
export interface ApiProblem {
  status: number;
  title: string;
  detail: string;
  /** Erreurs de validation par champ, présentes seulement pour un 400. */
  errors?: Record<string, string>;
}

/**
 * État d'un chargement asynchrone, modélisé par une union discriminée :
 * la propriété `status` indique laquelle des trois formes on manipule,
 * et TypeScript interdit par exemple de lire `data` tant que `status` n'est pas 'success'.
 */
export type LoadState<T> =
  { status: 'loading' } | { status: 'success'; data: T } | { status: 'error'; message: string };
