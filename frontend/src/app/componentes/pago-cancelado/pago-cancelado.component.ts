import {Component, OnInit, inject, signal} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';

import {PaymentsApiService} from '../../servicios/payments-api.service';

type CancelStatus = 'loading' | 'done' | 'missing' | 'error';

@Component({
  selector: 'app-pago-cancelado',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './pago-cancelado.component.html',
})
export class PagoCanceladoComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private paymentsApi = inject(PaymentsApiService);

  status = signal<CancelStatus>('loading');

  ngOnInit() {
    const stripeSessionId = this.route.snapshot.queryParamMap.get('session_id');

    if (!stripeSessionId) {
      this.status.set('missing');
      return;
    }

    this.paymentsApi.cancelCheckoutSession(stripeSessionId).subscribe({
      next: () => this.status.set('done'),
      error: () => this.status.set('error'),
    });
  }
}
