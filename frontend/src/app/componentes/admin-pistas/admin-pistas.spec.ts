import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPistasComponent } from './admin-pistas.component';

describe('AdminPistasComponent', () => {
  let component: AdminPistasComponent;
  let fixture: ComponentFixture<AdminPistasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminPistasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminPistasComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
