import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EmailDetailComponent } from './email-detail.component';

describe('Email Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmailDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EmailDetailComponent,
              resolve: { email: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EmailDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load email on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EmailDetailComponent);

      // THEN
      expect(instance.email).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
