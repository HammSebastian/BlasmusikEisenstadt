import {Injectable, inject, signal, computed} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable, throwError} from 'rxjs';
import {catchError, tap, finalize} from 'rxjs/operators'; // Importiere finalize
import {UserModel} from '../../models/essentials/user.model';
import {AuthResponse} from '../../models/essentials/authResponse.model';
import {LoginCredentials} from '../../models/essentials/loginCredentials.model';
import {environment} from '../../../../environment/environment';
import {RoleEnum} from '../../models/essentials/role.enum'; // WICHTIG: Stellen Sie sicher, dass dies eine String-Enum ist!
import {ProfileResponse} from '../../models/private/profile.response';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly http = inject(HttpClient);
    private readonly router = inject(Router);

    private readonly TOKEN_KEY = 'blasmusik_token';
    private readonly REFRESH_TOKEN_KEY = 'blasmusik_refresh_token';
    private readonly USER_KEY = 'blasmusik_user';

    private readonly baseUrl = environment.apiUrl;

    // Verwaltet den Authentifizierungsstatus der Anwendung
    private readonly authState = signal<{
        user: UserModel | null;
        isAuthenticated: boolean;
        isLoading: boolean;
    }>({
        user: this.getUserFromStorage(), // Benutzer aus dem lokalen Speicher initialisieren
        isAuthenticated: !!this.getToken(), // Initialen Authentifizierungsstatus bestimmen
        isLoading: false // Initialer Ladezustand
    });

    // Berechnete Signale für den einfachen Zugriff auf Authentifizierungsstatus-Eigenschaften
    public isAuthenticated = computed(() => this.authState().isAuthenticated);
    public currentUser = computed(() => this.authState().user);

    // Berechnete Signale zur Überprüfung spezifischer Benutzerrollen
    public isAdmin = computed(() => {
        const roles = this.authState().user?.roles || [];
        // Dies funktioniert korrekt, wenn RoleEnum.ADMIN ein String ist ('ROLE_ADMIN')
        return roles.includes(RoleEnum.ADMIN);
    });

    public isReporter = computed(() => {
        const roles = this.authState().user?.roles || [];
        return roles.includes(RoleEnum.REPORTER);
    });

    public isConductor = computed(() => {
        const roles = this.authState().user?.roles || [];
        return roles.includes(RoleEnum.CONDUCTOR);
    });

    public isSectionLeader = computed(() => {
        const roles = this.authState().user?.roles || [];
        return roles.includes(RoleEnum.SECTION_LEADER);
    });

    public isMusician = computed(() => {
        const roles = this.authState().user?.roles || [];
        return roles.includes(RoleEnum.MUSICIAN);
    });

    /**
     * Hilfsfunktion zum Konvertieren einer Rollen-Zeichenkette in das RoleEnum.
     * Sie behandelt Fälle mit oder ohne "ROLE_"-Präfix und protokolliert Warnungen für unbekannte Rollen.
     * @param roleStr Die Zeichenkettendarstellung der Rolle (z.B. "ROLE_ADMIN", "ADMIN").
     * @returns Der entsprechende RoleEnum-Wert oder null, wenn die Rollen-Zeichenkette nicht erkannt wird.
     */
    private mapStringToRoleEnum(roleStr: string): RoleEnum | null {
        // Normalisiere die Rollen-Zeichenkette in Großbuchstaben und stelle das "ROLE_"-Präfix sicher, um eine konsistente Übereinstimmung zu gewährleisten
        const normalizedRole = roleStr.toUpperCase().startsWith('ROLE_') ?
            roleStr.toUpperCase() :
            `ROLE_${roleStr.toUpperCase()}`;

        switch (normalizedRole) {
            // WICHTIG: Diese Case-Werte müssen mit den String-Werten in Ihrer RoleEnum übereinstimmen!
            case 'ROLE_ADMIN':
                return RoleEnum.ADMIN;
            case 'ROLE_REPORTER':
                return RoleEnum.REPORTER;
            case 'ROLE_CONDUCTOR':
                return RoleEnum.CONDUCTOR;
            case 'ROLE_MUSICIAN':
                return RoleEnum.MUSICIAN;
            default:
                return null;
        }
    }

    /**
     * Verarbeitet ein Array von Roh-Rollendaten (vom Backend oder Speicher),
     * ordnet sie RoleEnum-Werten zu und stellt eine Standardrolle "MUSICIAN" sicher,
     * wenn keine gültigen Rollen gefunden werden.
     * @param rawRoles Ein Array von Rollen, die Zeichenketten oder andere Typen sein können.
     * @returns Ein Array von `RoleEnum`-Werten.
     */
    private processAndMapRoles(rawRoles: any[] | undefined): RoleEnum[] {
        const rolesToProcess: string[] = Array.isArray(rawRoles)
            ? rawRoles.map((role: any) => String(role)) // Stelle sicher, dass alle Elemente Zeichenketten sind
            : [];

        const mappedRoles: RoleEnum[] = [];

        for (const roleStr of rolesToProcess) {
            const mappedRole = this.mapStringToRoleEnum(roleStr);
            if (mappedRole) {
                mappedRoles.push(mappedRole);
            }
        }

        // Wenn keine gültigen Rollen zugeordnet wurden, weise die Standardrolle MUSICIAN zu
        if (mappedRoles.length === 0) {
            mappedRoles.push(RoleEnum.MUSICIAN);
        }

        return mappedRoles;
    }

    /**
     * Überprüft, ob der aktuelle Benutzer eine der angegebenen Rollen hat.
     * @param allowedRoles Ein Array von Rollen-Zeichenketten oder RoleEnum-Werten, die überprüft werden sollen.
     * @returns Ein Boolean, der angibt, ob der Benutzer eine der erlaubten Rollen hat.
     */
    public hasAnyRole(allowedRoles: (string | RoleEnum)[]): boolean {
        // Wenn der Benutzer nicht authentifiziert ist oder keine Rollen hat, sofort false zurückgeben.
        if (!this.isAuthenticated() || !this.authState().user?.roles) {
            return false;
        }

        const userRoles = this.authState().user!.roles; // Benutzerrollen sind bereits RoleEnum[]

        // Normalisiere die erlaubten Rollen zu RoleEnum für einen konsistenten Vergleich.
        const normalizedAllowedRoles: RoleEnum[] = allowedRoles
            .map(role => typeof role === 'string' ? this.mapStringToRoleEnum(role) : role)
            .filter((role): role is RoleEnum => role !== null); // Filtere alle nicht zugeordneten Rollen heraus

        // Überprüfe, ob eine der Benutzerrollen in den normalisierten erlaubten Rollen enthalten ist.
        return userRoles.some(userRole => normalizedAllowedRoles.includes(userRole));
    }

    constructor() {
        // Der Konstruktor ist absichtlich leer, da Abhängigkeiten direkt injiziert und der Zustand initialisiert werden.
    }

    /**
     * Behandelt die Benutzeranmeldung, indem Anmeldeinformationen an das Backend gesendet werden.
     * Verarbeitet die Authentifizierungsantwort, speichert Token und Benutzerdaten,
     * aktualisiert den Authentifizierungsstatus und leitet den Benutzer basierend auf seinen Rollen weiter.
     * @param credentials Die Anmeldeinformationen (Benutzername und Passwort).
     * @returns Ein Observable der AuthResponse.
     */
    login(credentials: LoginCredentials): Observable<AuthResponse> {
        this.setLoading(true); // Ladezustand auf true setzen

        return this.http.post<any>(this.baseUrl + '/auth/login', credentials).pipe(
            tap({
                next: (response) => {
                    try {
                        let authResponseData = response;

                        // Überprüfe, ob die Antwort eine 'data'-Eigenschaft enthält (häufiges API-Wrapper-Muster)
                        // Falls ja, verwende diese für Benutzerdaten und Token.
                        if (response && response.data) {
                            authResponseData = response.data;
                        }

                        // Das 'user'-Objekt ist in Ihrer aktuellen Backend-Struktur die authResponseData selbst.
                        const user = authResponseData;

                        // Robuste Validierung für wesentliche Benutzereigenschaften
                        // Überprüfen Sie, ob 'user' ein Objekt ist und 'id' sowie 'roles' Eigenschaften besitzt.
                        if (!user || user.id == null || !Array.isArray(user.roles)) {
                            throw new Error('Ungültiges Antwortformat vom Server (fehlende ID oder Rolleninformationen).');
                        }

                        // Rollen aus der Backend-Antwort verarbeiten und zuordnen
                        const userRolesMapped = this.processAndMapRoles(user.roles);

                        // Erstelle ein UserModel mit dem korrekt zugeordneten RoleEnum-Array
                        const userModel: UserModel = {
                            id: user.id, // Direkt aus dem Benutzerobjekt verwenden
                            name: user.username || user.name || user.email || 'Unbekannter Benutzer',
                            email: user.email || '',
                            roles: userRolesMapped
                        };

                        // Authentifizierungsdaten (Token, Roh-Benutzerdaten) im lokalen Speicher speichern
                        // authResponseData übergeben, da es die Token enthält.
                        this.setAuthData(authResponseData);
                        // Aktualisiere den Authentifizierungsstatus der Anwendung mit dem verarbeiteten UserModel
                        this.updateAuthState(userModel, true);
                    } catch (error) {
                        this.clearAuthData(); // Alle teilweisen Authentifizierungsdaten bei Fehler löschen
                        throw error; // Erneut werfen, um vom catchError-Operator abgefangen zu werden
                    }
                },
                error: (error) => {
                    this.setLoading(false); // Stelle sicher, dass der Ladezustand zurückgesetzt wird
                    this.clearAuthData(); // Authentifizierungsdaten bei Anmeldefehler löschen
                    throw error; // Erneut werfen, um vom catchError-Operator abgefangen zu werden
                }
            }),
            catchError((error: any) => {
                this.setLoading(false); // Stelle sicher, dass der Ladezustand zurückgesetzt wird
                this.clearAuthData(); // Authentifizierungsdaten bei Anmeldefehler löschen

                // Benutzerfreundliche Fehlermeldungen basierend auf dem HTTP-Status oder der Backend-Nachricht bereitstellen
                if (error.error && error.error.message) {
                    return throwError(() => new Error(error.error.message));
                } else if (error.status === 401) {
                    return throwError(() => new Error('Ungültige Anmeldedaten. Bitte überprüfen Sie Ihren Benutzernamen und Ihr Passwort.'));
                } else if (error.status === 403) {
                    return throwError(() => new Error('Zugriff verweigert. Sie haben keine Berechtigung für diese Aktion.'));
                } else if (error.status === 0) {
                    return throwError(() => new Error('Verbindungsfehler. Bitte überprüfen Sie Ihre Internetverbindung und versuchen Sie es erneut.'));
                } else {
                    return throwError(() => new Error('Ein unbekannter Fehler ist aufgetreten. Bitte versuchen Sie es später erneut.'));
                }
            })
        );
    }

    /**
     * Meldet den aktuellen Benutzer ab, indem die Sitzung im Backend für ungültig erklärt wird.
     * Löscht den lokalen Speicher und setzt den Authentifizierungsstatus zurück.
     */
    logout(): void {
        this.http.post(this.baseUrl + '/auth/logout', {}).subscribe({
            next: () => {
                this.clearAuthData(); // Token und Benutzerdaten aus dem lokalen Speicher löschen
                this.updateAuthState(null, false); // Authentifizierungsstatus zurücksetzen
                this.router.navigate(['/']); // Zur Startseite weiterleiten
            },
            error: (error) => {
                // Auch wenn die Backend-Abmeldung fehlschlägt, lokale Daten löschen, um sicherzustellen, dass der Benutzer clientseitig abgemeldet wird
                this.clearAuthData();
                this.updateAuthState(null, false);
                this.router.navigate(['/']);
            }
        });
    }

    /**
     * Ruft das Authentifizierungstoken aus dem lokalen Speicher ab.
     * @returns Die Token-Zeichenkette oder null, wenn nicht gefunden.
     */
    getToken(): string | null {
        // Schutz vor nicht verfügbarem localStorage (z.B. während des Server-Side-Renderings)
        if (typeof localStorage === 'undefined') return null;
        return localStorage.getItem(this.TOKEN_KEY);
    }

    /**
     * Ruft Benutzerdaten aus dem lokalen Speicher ab und ordnet Rollen dem RoleEnum zu.
     * @returns Das UserModel oder null, wenn keine Benutzerdaten gefunden werden oder diese ungültig sind.
     */
    private getUserFromStorage(): UserModel | null {
        if (typeof localStorage === 'undefined') return null;
        const userStr = localStorage.getItem(this.USER_KEY);
        if (!userStr) return null;

        try {
            const storedUser = JSON.parse(userStr);
            // Rollen aus den gespeicherten Benutzerdaten verarbeiten und zuordnen
            const rolesMapped = this.processAndMapRoles(storedUser.roles);

            return {
                id: storedUser.id || 'unknown',
                name: storedUser.username || storedUser.name || 'Unbekannter Benutzer',
                email: storedUser.email || '',
                roles: rolesMapped
            };
        } catch (error) {
            localStorage.removeItem(this.USER_KEY); // Beschädigte Daten löschen
            return null;
        }
    }

    /**
     * Speichert authentifizierungsbezogene Daten (Token, Benutzerinformationen) im lokalen Speicher.
     * @param response Die rohe Authentifizierungsantwort vom Backend.
     */
    private setAuthData(response: any): void {
        if (typeof localStorage === 'undefined') return;

        try {
            // Token, Refresh-Token und Benutzerdaten extrahieren, wobei potenzielle 'data'-Wrapper behandelt werden
            const token = response.token || (response.data && response.data.token);
            const refreshToken = response.refreshToken || (response.data && response.data.refreshToken);
            // Hier sollte 'user' direkt die Eigenschaften des Benutzers enthalten, nicht ein verschachteltes Objekt
            const user = response.user || (response.data && response.data.user) || response; // Fallback, falls response selbst der User ist

            if (token) {
                localStorage.setItem(this.TOKEN_KEY, token);
            }
            if (refreshToken) {
                localStorage.setItem(this.REFRESH_TOKEN_KEY, refreshToken);
            }
            if (user) {
                // Rollen als Zeichenketten im localStorage speichern; sie werden beim Abrufen oder Aktualisieren des Authentifizierungsstatus dem RoleEnum zugeordnet.
                const userDataToStore = {
                    id: user.id,
                    username: user.username || user.email,
                    email: user.email,
                    roles: Array.isArray(user.roles) ? user.roles.map((role: any) => String(role)) : []
                };
                localStorage.setItem(this.USER_KEY, JSON.stringify(userDataToStore));
            }
        } catch (error) {
            this.clearAuthData(); // Alle teilweisen Daten löschen, wenn ein Fehler beim Speichern auftritt
            throw new Error('Fehler beim Verarbeiten der Anmeldedaten für den Speicher.');
        }
    }

    /**
     * Löscht alle authentifizierungsbezogenen Daten aus dem lokalen Speicher.
     */
    private clearAuthData(): void {
        if (typeof localStorage === 'undefined') return;
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.REFRESH_TOKEN_KEY);
        localStorage.removeItem(this.USER_KEY);
    }

    /**
     * Aktualisiert das interne Authentifizierungsstatus-Signal.
     * Diese Methode ist für das Setzen des aktuellen Benutzers und des Authentifizierungsstatus verantwortlich.
     * @param user Das UserModel-Objekt oder null, wenn nicht authentifiziert.
     * @param isAuthenticated Boolean, der angibt, ob der Benutzer authentifiziert ist.
     */
    private updateAuthState(user: UserModel | null, isAuthenticated: boolean): void {
        try {
            this.authState.set({
                user: user,
                isAuthenticated: isAuthenticated && !!user, // Stelle sicher, dass isAuthenticated nur true ist, wenn das Benutzerobjekt existiert
                isLoading: false // Ladezustand nach der Aktualisierung zurücksetzen
            });

        } catch (error) {
            // Bei Fehler auf einen vollständig nicht authentifizierten Zustand zurückfallen
            this.authState.set({
                user: null,
                isAuthenticated: false,
                isLoading: false
            });
        }
    }

    /**
     * Setzt den Ladezustand des Authentifizierungsdienstes.
     * @param isLoading Boolean, der angibt, ob ein Authentifizierungsvorgang läuft.
     */
    private setLoading(isLoading: boolean): void {
        this.authState.update(state => ({
            ...state,
            isLoading
        }));
    }

    /**
     * Ruft das Profil des aktuellen Benutzers vom Backend ab.
     * Verarbeitet die Antwort und aktualisiert den Authentifizierungsstatus mit den Profildaten.
     * @returns Ein Observable der ProfileResponse.
     */
    fetchUserProfile(): Observable<ProfileResponse> {
        this.setLoading(true); // Ladezustand auf true setzen

        return this.http.get<ProfileResponse>(this.baseUrl + '/profile').pipe(
            tap({
                next: (response) => {
                    try {
                        // Rollen aus der Profilantwort verarbeiten und zuordnen
                        const rolesMapped = this.processAndMapRoles(response.roles);

                        // UserModel aus der Profilantwort konstruieren
                        const user: UserModel = {
                            id: response.id,
                            name: response.username || 'Unbekannter Benutzer', // Benutzernamen oder Standard verwenden
                            email: response.email || '',
                            roles: rolesMapped,
                        };

                        this.updateAuthState(user, true); // Status mit dem abgerufenen Profil aktualisieren
                    } catch (error) {
                        // Wenn die Verarbeitung fehlschlägt, einen Standardbenutzer mit der Rolle MUSICIAN erstellen und den Status aktualisieren
                        const defaultUser: UserModel = {
                            id: 'unknown',
                            name: 'Unbekannter Benutzer',
                            email: '',
                            roles: [RoleEnum.MUSICIAN]
                        };
                        this.updateAuthState(defaultUser, true);
                    }
                },
                error: (error) => {
                    // Bei Fehler einen Standardbenutzer mit der Rolle MUSICIAN erstellen und den Status aktualisieren
                    const defaultUser: UserModel = {
                        id: 'unknown',
                        name: 'Unbekannter Benutzer',
                        email: '',
                        roles: [RoleEnum.MUSICIAN]
                    };
                    this.updateAuthState(defaultUser, true);
                }
            }),
            finalize(() => {
                this.setLoading(false); // Stelle sicher, dass der Ladezustand unabhängig vom Erfolg oder Fehler zurückgesetzt wird
            })
        );
    }

    /**
     * Gibt die Rollen des aktuell angemeldeten Benutzers zurück.
     * Diese Methode wird vom `roleGuard` verwendet.
     * @returns Ein Array von `RoleEnum`-Werten, die die Rollen des Benutzers darstellen, oder ein leeres Array, wenn der Benutzer nicht angemeldet ist oder keine Rollen hat.
     */
    getUserRoles(): RoleEnum[] {
        console.log(this.authState().user?.roles);
        // Greife direkt auf die Rollen des aktuellen Benutzers zu, der im authState-Signal gespeichert ist.
        return this.authState().user?.roles || [];
    }

    isLoggedInAsAdmin() {
        return this.getUserRoles().includes(RoleEnum.ADMIN);
    }


    isLoggedInAsReporter() {
        return this.getUserRoles().includes(RoleEnum.REPORTER);
    }

    isLoggedInAsConductor() {
        return this.getUserRoles().includes(RoleEnum.CONDUCTOR);
    }

    isLoggedInAsSectionLeader() {
        return this.getUserRoles().includes(RoleEnum.SECTION_LEADER);
    }

    isLoggedInAsMusician() {
        return this.getUserRoles().includes(RoleEnum.MUSICIAN);
    }

    isLoggedIn() {
        return this.authState().isAuthenticated;
    }

    getCurrentUser() {
        return this.authState().user;
    }
}
