import { HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { Observable, of, throwError } from 'rxjs';
import { provideFrenchLocale } from '../../../core/i18n/french-locale';
import { FRENCH_PAGINATOR_PROVIDER } from '../../../shared/material/french-paginator-intl';
import { Page } from '../../../core/api/api.models';
import { Product, ProductQuery } from '../product.model';
import { ProductService } from '../product.service';
import { ProductList } from './product-list';

const tasse: Product = {
  id: 1,
  sku: 'TASSE-01',
  name: 'Tasse en grès',
  description: null,
  unitPrice: 12.5,
  stockAvailable: 4,
  active: true,
  createdAt: '2026-10-27T10:00:00Z',
  updatedAt: '2026-10-27T10:00:00Z',
};

function page(content: Product[]): Page<Product> {
  return { content, page: 0, size: 10, totalElements: content.length, totalPages: 1 };
}

/**
 * Tests du composant liste : le vrai ProductService est remplacé par une doublure
 * dont on contrôle la réponse, pour tester l'affichage de chaque état.
 */
describe('ProductList', () => {
  let searchResult: Observable<Page<Product>>;
  let lastQuery: ProductQuery | undefined;

  async function render() {
    await TestBed.configureTestingModule({
      imports: [ProductList],
      providers: [
        provideRouter([]),
        provideFrenchLocale(),
        FRENCH_PAGINATOR_PROVIDER,
        {
          provide: ProductService,
          useValue: {
            search: (query: ProductQuery) => {
              lastQuery = query;
              return searchResult;
            },
          },
        },
      ],
    }).compileComponents();
    const fixture = TestBed.createComponent(ProductList);
    await fixture.whenStable();
    fixture.detectChanges();
    return fixture.nativeElement as HTMLElement;
  }

  it('affiche les produits renvoyés par le service', async () => {
    searchResult = of(page([tasse]));

    const element = await render();

    expect(element.querySelectorAll('tr[mat-row]').length).toBe(1);
    expect(element.textContent).toContain('TASSE-01');
    expect(element.textContent).toContain('12,50');
    expect(element.textContent).toContain('Actif');
    expect(element.textContent).toContain('1 – 1 sur 1');
  });

  it('affiche un message quand aucun produit ne correspond', async () => {
    searchResult = of(page([]));

    const element = await render();

    expect(element.textContent).toContain('Aucun produit ne correspond');
  });

  it("affiche le message d'erreur de l'API", async () => {
    searchResult = throwError(
      () =>
        new HttpErrorResponse({
          status: 400,
          error: { status: 400, title: 'Paramètre invalide', detail: 'Tri impossible.' },
        }),
    );

    const element = await render();

    expect(element.querySelector('[role="alert"]')?.textContent).toContain('Tri impossible.');
  });

  it('charge la première page triée par nom au démarrage', async () => {
    searchResult = of(page([]));

    await render();

    expect(lastQuery).toEqual({ page: 0, size: 10, sort: 'name,asc' });
  });
});
