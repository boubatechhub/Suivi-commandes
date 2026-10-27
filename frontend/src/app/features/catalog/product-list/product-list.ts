import { CurrencyPipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { catchError, map, of, startWith, switchMap } from 'rxjs';
import { errorMessage } from '../../../core/api/api-error';
import { LoadState, Page } from '../../../core/api/api.models';
import { Product, ProductQuery } from '../product.model';
import { ProductService } from '../product.service';

/** Liste paginée du catalogue, avec recherche par SKU ou nom. */
@Component({
  selector: 'app-product-list',
  imports: [
    CurrencyPipe,
    ReactiveFormsModule,
    RouterLink,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatTableModule,
  ],
  templateUrl: './product-list.html',
  styleUrl: './product-list.scss',
})
export class ProductList {
  private readonly productService = inject(ProductService);

  protected readonly displayedColumns = [
    'sku',
    'name',
    'unitPrice',
    'stockAvailable',
    'active',
    'actions',
  ];
  protected readonly searchControl = new FormControl('', { nonNullable: true });

  /** Critères de recherche courants : les modifier relance automatiquement le chargement. */
  protected readonly query = signal<ProductQuery>({ page: 0, size: 10, sort: 'name,asc' });
  protected readonly state = signal<LoadState<Page<Product>>>({ status: 'loading' });

  constructor() {
    // Signal → Observable : chaque nouvelle valeur de `query` déclenche une requête.
    // switchMap annule la requête précédente si une nouvelle arrive avant sa réponse :
    // on n'affiche jamais un résultat périmé.
    toObservable(this.query)
      .pipe(
        switchMap((query) =>
          this.productService.search(query).pipe(
            map((page): LoadState<Page<Product>> => ({ status: 'success', data: page })),
            catchError((error) =>
              of<LoadState<Page<Product>>>({ status: 'error', message: errorMessage(error) }),
            ),
            startWith<LoadState<Page<Product>>>({ status: 'loading' }),
          ),
        ),
        takeUntilDestroyed(),
      )
      .subscribe((state) => this.state.set(state));
  }

  protected search(): void {
    this.query.update((q) => ({ ...q, q: this.searchControl.value, page: 0 }));
  }

  protected changePage(event: PageEvent): void {
    this.query.update((q) => ({ ...q, page: event.pageIndex, size: event.pageSize }));
  }

  protected retry(): void {
    this.query.update((q) => ({ ...q }));
  }
}
