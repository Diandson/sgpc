import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { StockageDetailComponent } from './stockage-detail.component';

describe('Stockage Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockageDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: StockageDetailComponent,
              resolve: { stockage: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StockageDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load stockage on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StockageDetailComponent);

      // THEN
      expect(instance.stockage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
