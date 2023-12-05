import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ColisageDetailComponent } from './colisage-detail.component';

describe('Colisage Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ColisageDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ColisageDetailComponent,
              resolve: { colisage: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ColisageDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load colisage on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ColisageDetailComponent);

      // THEN
      expect(instance.colisage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
