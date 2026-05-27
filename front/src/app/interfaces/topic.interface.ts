export interface Topic {
    id: number;
    name: string;
    /** Indique si l'utilisateur connecté est abonné à ce thème */
    subscribed: boolean;
}