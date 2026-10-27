/** Produit tel que renvoyé par l'API (ProductResponse). */
export interface Product {
  id: number;
  sku: string;
  name: string;
  description: string | null;
  unitPrice: number;
  stockAvailable: number;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

/** Données envoyées pour créer ou modifier un produit (ProductRequest). */
export interface ProductRequest {
  sku: string;
  name: string;
  description: string | null;
  unitPrice: number;
  stockAvailable: number;
  active: boolean;
}

/** Paramètres de recherche du catalogue. */
export interface ProductQuery {
  q?: string;
  page: number;
  size: number;
  sort?: string;
}
