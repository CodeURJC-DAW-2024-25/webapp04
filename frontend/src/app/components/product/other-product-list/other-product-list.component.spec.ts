import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OtherProductListComponent } from './other-product-list.component';

describe('OtherProductListComponent', () => {
  let component: OtherProductListComponent;
  let fixture: ComponentFixture<OtherProductListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OtherProductListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OtherProductListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
