import { writable } from 'svelte/store';

export const loggerStore = writable([]);
export const queryStore = writable("");
export const errorStore = writable("");