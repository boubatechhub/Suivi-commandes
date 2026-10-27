import { HttpErrorResponse } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ProductService } from '../product.service';
import { ProductForm } from './product-form';

/** Tests du formulaire : validation côté client, envoi, et affichage des erreurs du serveur. */
describe('ProductForm', () => {
  let fixture: ComponentFixture<ProductForm>;
  let element: HTMLElement;
  const service = {
    create: vi.fn(),
    update: vi.fn(),
    getById: vi.fn(),
  };

  beforeEach(async () => {
    vi.resetAllMocks();
    await TestBed.configureTestingModule({
      imports: [ProductForm],
      providers: [provideRouter([]), { provide: ProductService, useValue: service }],
    }).compileComponents();
    fixture = TestBed.createComponent(ProductForm);
    element = fixture.nativeElement;
    await fixture.whenStable();
  });

  function setInput(name: string, value: string): void {
    const input = element.querySelector<HTMLInputElement>(`[formcontrolname="${name}"]`)!;
    input.value = value;
    input.dispatchEvent(new Event('input'));
  }

  async function submit(): Promise<void> {
    element.querySelector('form')!.dispatchEvent(new Event('submit'));
    fixture.detectChanges();
    await fixture.whenStable();
  }

  it("n'appelle pas l'API et affiche les erreurs si le formulaire est invalide", async () => {
    await submit();

    expect(service.create).not.toHaveBeenCalled();
    expect(element.textContent).toContain('Le SKU est obligatoire.');
    expect(element.textContent).toContain('Le nom est obligatoire.');
  });

  it('envoie le produit puis revient au catalogue', async () => {
    const navigate = vi.spyOn(TestBed.inject(Router), 'navigate').mockResolvedValue(true);
    service.create.mockReturnValue(of({ id: 1 }));
    setInput('sku', 'TASSE-01');
    setInput('name', 'Tasse');
    setInput('unitPrice', '12.5');
    setInput('stockAvailable', '3');

    await submit();

    expect(service.create).toHaveBeenCalledWith({
      sku: 'TASSE-01',
      name: 'Tasse',
      description: null,
      unitPrice: 12.5,
      stockAvailable: 3,
      active: true,
    });
    expect(navigate).toHaveBeenCalledWith(['/catalogue']);
  });

  it('affiche le conflit renvoyé par le serveur (SKU déjà utilisé)', async () => {
    service.create.mockReturnValue(
      throwError(
        () =>
          new HttpErrorResponse({
            status: 409,
            error: { status: 409, title: 'Conflit', detail: 'Le SKU TASSE-01 est déjà utilisé.' },
          }),
      ),
    );
    setInput('sku', 'TASSE-01');
    setInput('name', 'Tasse');

    await submit();

    expect(element.querySelector('[role="alert"]')?.textContent).toContain('déjà utilisé');
  });

  it('place les erreurs de validation du serveur sous les champs concernés', async () => {
    service.create.mockReturnValue(
      throwError(
        () =>
          new HttpErrorResponse({
            status: 400,
            error: {
              status: 400,
              title: 'Données invalides',
              detail: 'Un ou plusieurs champs sont invalides.',
              errors: { name: 'ne doit pas être vide' },
            },
          }),
      ),
    );
    setInput('sku', 'TASSE-01');
    setInput('name', 'Tasse');

    await submit();

    expect(element.textContent).toContain('ne doit pas être vide');
  });
});
