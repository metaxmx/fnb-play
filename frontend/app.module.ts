import {NgModule} from '@angular/core'
import {BrowserModule} from '@angular/platform-browser'
import {RouterModule} from '@angular/router'
import {APP_ROUTES} from "./app.routes"

import {AppComponent}  from './app.component'
import {FNB_COMPONENTS} from "./views/components"
import {FNB_SERVICE_PROVIDERS} from "./services/services"
import {FNB_UTILS_PROVIDERS} from "./util/utils"
import {FNB_PIPES} from "./pipes/pipes"

import {HttpModule, Http} from "@angular/http"
import {FormsModule} from '@angular/forms'
import {TranslateModule, TranslateLoader, TranslateStaticLoader} from "ng2-translate"
import {Ng2BootstrapModule, AlertModule} from "ng2-bootstrap";


const FNB_TRANSLATE_CONFIG = {
    provide: TranslateLoader,
    useFactory: (http: Http) => new TranslateStaticLoader(http, '/assets/i18n', '.json'),
    deps: [Http]
};

@NgModule({
    imports: [
        BrowserModule,
        RouterModule.forRoot(APP_ROUTES),
        HttpModule,
        FormsModule,
        TranslateModule.forRoot(FNB_TRANSLATE_CONFIG),
        Ng2BootstrapModule, AlertModule
    ],
    declarations: [
        AppComponent,
        FNB_COMPONENTS,
        FNB_PIPES
    ],
    providers: [
        FNB_SERVICE_PROVIDERS,
        FNB_UTILS_PROVIDERS
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}