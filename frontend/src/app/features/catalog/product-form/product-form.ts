import { Component, OnInit, inject, input, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { errorMessage, toApiProblem } from '../../../core/api/api-error';
import { Product, ProductRequest } from '../product.model';
import { ProductService } from '../product.service';

/**
 * Formulaire de création et de modification d'un produit (Reactive Forms).
 * Sans `id` : création. Avec `id` (paramètre de route) : modification.
 */
@Component({
  selector: 'app-product-form',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatButtonModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressBarModule,
  ],
  templateUrl: './product-form.html',
  styleUrl: './product-form.scss',
})
export class ProductForm implements OnInit {
  private readonly productService = inject(ProductService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  /** Paramètre de route :id, absent en création. */
  readonly id = input<string>();

  protected readonly loading = signal(false);
  protected readonly saving = signal(false);
  protected readonly serverError = signal<string | null>(null);

  // Les mêmes règles que côté serveur, pour guider l'utilisateur avant l'envoi.
  // Le serveur reste la référence : il revalide tout.
  protected readonly form = inject(FormBuilder).nonNullable.group({
    sku: [
      '',
      [Validators.required, Validators.maxLength(40), Validators.pattern(/^[A-Za-z0-9-]+$/)],
    ],
    name: ['', [Validators.required, Validators.maxLength(120)]],
    description: ['', Validators.maxLength(1000)],
    unitPrice: [0, [Validators.required, Validators.min(0)]],
    stockAvailable: [0, [Validators.required, Validators.min(0), Validators.pattern(/^\d+$/)]],
    active: [true],
  });

  protected get isEdit(): boolean {
    return !!this.id();
  }

  /** Hook du cycle de vie : appelé une fois, après que les inputs (dont `id`) ont reçu leur valeur. */
  ngOnInit(): void {
    const id = this.id();
    if (!id) {
      return;
    }
    this.loading.set(true);
    this.productService.getById(Number(id)).subscribe({
      next: (product) => {
        this.fill(product);
        this.loading.set(false);
      },
      error: (error) => {
        this.serverError.set(errorMessage(error));
        this.loading.set(false);
      },
    });
  }

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.serverError.set(null);

    const value = this.form.getRawValue(); // getRawValue inclut le SKU désactivé en modification
    const request: ProductRequest = { ...value, description: value.description || null };
    const id = this.id();
    const save$: Observable<Product> = id
      ? this.productService.update(Number(id), request)
      : this.productService.create(request);

    save$.subscribe({
      next: () => {
        this.snackBar.open('Produit enregistré', 'OK', { duration: 3000 });
        this.router.navigate(['/catalogue']);
      },
      error: (error) => {
        this.saving.set(false);
        this.applyServerErrors(error);
      },
    });
  }

  /** Affiche les erreurs renvoyées par l'API sous les champs concernés. */
  private applyServerErrors(error: unknown): void {
    const problem = toApiProblem(error);
    if (problem?.status === 400 && problem.errors) {
      for (const [field, message] of Object.entries(problem.errors)) {
        this.form.get(field)?.setErrors({ server: message });
      }
    }
    this.serverError.set(errorMessage(error));
  }

  private fill(product: Product): void {
    this.form.setValue({
      sku: product.sku,
      name: product.name,
      description: product.description ?? '',
      unitPrice: product.unitPrice,
      stockAvailable: product.stockAvailable,
      active: product.active,
    });
    // Le SKU est un identifiant métier : il ne se modifie pas (règle vérifiée aussi par l'API)
    this.form.controls.sku.disable();
  }
}
