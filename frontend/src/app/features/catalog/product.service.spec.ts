import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ProductService } from './product.service';

/**
 * Tests du service HTTP : aucune requête ne part vraiment.
 * HttpTestingController intercepte les appels, on vérifie l'URL, la méthode et les paramètres,
 * puis on renvoie une réponse simulée avec flush().
 */
describe('ProductService', () => {
  let service: ProductService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ProductService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify()); // échoue si une requête inattendue est restée sans réponse

  it('envoie la recherche, la pagination et le tri en paramètres', () => {
    service.search({ q: '  tasse ', page: 2, size: 10, sort: 'name,asc' }).subscribe();

    const req = http.expectOne((r) => r.url === '/api/products');
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('q')).toBe('tasse');
    expect(req.request.params.get('page')).toBe('2');
    expect(req.request.params.get('size')).toBe('10');
    expect(req.request.params.get('sort')).toBe('name,asc');
    req.flush({ content: [], page: 2, size: 10, totalElements: 0, totalPages: 0 });
  });

  it("n'envoie pas de paramètre q quand la recherche est vide", () => {
    service.search({ q: '   ', page: 0, size: 10 }).subscribe();

    const req = http.expectOne((r) => r.url === '/api/products');
    expect(req.request.params.has('q')).toBe(false);
    req.flush({ content: [], page: 0, size: 10, totalElements: 0, totalPages: 0 });
  });

  it('crée un produit avec POST et met à jour avec PUT', () => {
    const body = {
      sku: 'TASSE-01',
      name: 'Tasse',
      description: null,
      unitPrice: 12.5,
      stockAvailable: 3,
      active: true,
    };

    service.create(body).subscribe();
    const post = http.expectOne('/api/products');
    expect(post.request.method).toBe('POST');
    expect(post.request.body).toEqual(body);
    post.flush({ id: 1, ...body });

    service.update(1, body).subscribe();
    const put = http.expectOne('/api/products/1');
    expect(put.request.method).toBe('PUT');
    put.flush({ id: 1, ...body });
  });
});
