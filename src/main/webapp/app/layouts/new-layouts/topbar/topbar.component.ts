import { Component, OnInit, Output, EventEmitter, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { DOCUMENT } from '@angular/common';
import { CookieService } from 'ngx-cookie-service';
import { TranslateService } from '@ngx-translate/core';
import { LanguageService } from '../../../core/services/language.service';
import { LoginService } from '../../../login/login.service';

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss'],
})

/**
 * Topbar component
 */
export class TopbarComponent implements OnInit {
  element: HTMLElement | undefined;
  cookieValue: string | undefined;
  flagvalue: any;
  countryName: string | string[] | undefined;
  valueset: any;

  constructor(
    @Inject(DOCUMENT) private document: any,
    private router: Router,
    private loginService: LoginService,
    public languageService: LanguageService,
    public _cookiesService: CookieService,
  ) {}

  listLang = [
    { text: 'English', flag: '../../../../content/images/flags/us.jpg', lang: 'en' },
    { text: 'Spanish', flag: '../../../../content/images/flags/spain.jpg', lang: 'es' },
    { text: 'German', flag: '../../../../content/images/flags/germany.jpg', lang: 'de' },
    { text: 'Italian', flag: '../../../../content/images/flags/italy.jpg', lang: 'it' },
    { text: 'Russian', flag: '../../../../content/images/flags/russia.jpg', lang: 'ru' },
  ];

  openMobileMenu: boolean | undefined;

  @Output() settingsButtonClicked = new EventEmitter();
  @Output() mobileMenuButtonClicked = new EventEmitter();

  ngOnInit() {
    this.openMobileMenu = false;
    this.element = document.documentElement;

    this.cookieValue = this._cookiesService.get('lang');
    const val = this.listLang.filter(x => x.lang === this.cookieValue);
    this.countryName = val.map(element => element.text);
    if (val.length === 0) {
      if (this.flagvalue === undefined) {
        this.valueset = '../../../../content/images/flags/us.jpg';
      }
    } else {
      this.flagvalue = val.map(element => element.flag);
    }
  }

  setLanguage(text: string, lang: string, flag: string) {
    this.countryName = text;
    this.flagvalue = flag;
    this.cookieValue = lang;
    this.languageService.setLanguage(lang);
  }

  /**
   * Toggles the right sidebar
   */
  toggleRightSidebar() {
    this.settingsButtonClicked.emit();
  }

  /**
   * Toggle the menu bar when having mobile screen
   */
  toggleMobileMenu(event: any) {
    event.preventDefault();
    this.mobileMenuButtonClicked.emit();
  }

  /**
   * Logout the user
   */
  logout() {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

  /**
   * Fullscreen method
   */
}
