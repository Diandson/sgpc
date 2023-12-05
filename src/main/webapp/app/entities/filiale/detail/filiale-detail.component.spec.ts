import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FilialeDetailComponent } from './filiale-detail.component';

describe('Filiale Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FilialeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FilialeDetailComponent,
              resolve: { filiale: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FilialeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load filiale on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FilialeDetailComponent);

      // THEN
      expect(instance.filiale).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
