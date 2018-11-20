import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { VgCoreModule } from 'videogular2/core';
import { VgControlsModule } from 'videogular2/controls';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthGuard } from './shared';
import { AssessmentQuestionsService } from './layout/assessment-questions/assessment-questions.service';

// AoT requires an exported function for factories
export const createTranslateLoader = (http: HttpClient) => {
    /* for development
    return new TranslateHttpLoader(
        http,
        '/start-angular/SB-Admin-BS4-Angular-6/master/dist/assets/i18n/',
        '.json'
    ); */
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
};

@NgModule({
    imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: createTranslateLoader,
                deps: [HttpClient]
            }
        }),
        AppRoutingModule,
        VgCoreModule,
        VgControlsModule
    ],
    declarations: [AppComponent],
    providers: [AuthGuard,AssessmentQuestionsService],
    bootstrap: [AppComponent]
})
export class AppModule {}
