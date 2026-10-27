import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../../core/api/api.models';
import { Product, ProductQuery, ProductRequest } from './product.model';

/**
 * Accès HTTP au catalogue. Le service ne fait que parler à l'API :
 * l'état de l'écran (chargement, erreurs) reste dans les composants.
 *
 * `providedIn: 'root'` : une seule instance pour toute l'application,
 * créée au premier besoin.
 */
@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/products';

  search(query: ProductQuery): Observable<Page<Product>> {
    let params = new HttpParams().set('page', query.page).set('size', query.size);
    if (query.q?.trim()) {
      params = params.set('q', query.q.trim());
    }
    if (query.sort) {
      params = params.set('sort', query.sort);
    }
    return this.http.get<Page<Product>>(this.baseUrl, { params });
  }

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/${id}`);
  }

  create(request: ProductRequest): Observable<Product> {
    return this.http.post<Product>(this.baseUrl, request);
  }

  update(id: number, request: ProductRequest): Observable<Product> {
    return this.http.put<Product>(`${this.baseUrl}/${id}`, request);
  }
}
