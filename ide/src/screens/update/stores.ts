import { writable } from 'svelte/store';

export const connectionMessages = writable({
    setSource: (s) => (x, y) => { },
    addConnection: (d) => [(x, y) => { }, () => { }],
});