import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}

console.log('WF_SERVER', environment.WF_SERVER);
console.log('WF_SCHEDULER', environment.WF_SCHEDULER);

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
